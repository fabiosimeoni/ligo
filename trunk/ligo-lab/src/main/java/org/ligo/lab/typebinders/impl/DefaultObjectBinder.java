/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static java.lang.String.*;
import static org.ligo.lab.typebinders.Bind.Mode.*;
import static org.ligo.lab.typebinders.Key.*;
import static org.ligo.lab.typebinders.kinds.Kind.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Qualifier;
import javax.xml.namespace.QName;

import org.ligo.lab.data.DataProvider;
import org.ligo.lab.data.Provided;
import org.ligo.lab.data.StructureProvider;
import org.ligo.lab.typebinders.Bind;
import org.ligo.lab.typebinders.Environment;
import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.ObjectBinder;
import org.ligo.lab.typebinders.TypeBinder;
import org.ligo.lab.typebinders.kinds.Kind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link TypeBinder} for arbitrary object structures.
 * 
 * @author Fabio Simeoni
 *
 */
public class DefaultObjectBinder<TYPE> extends AbstractBinder<TYPE> implements ObjectBinder<TYPE> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultObjectBinder.class);
	
	private final Environment env;
	
	private ConstructorDef constructorDef;
	private List<MethodDef> methodDefs = new LinkedList<MethodDef>();
	
	private Map<QName,TypeBinder<?>> binders = new HashMap<QName, TypeBinder<?>>();
	
	
	public DefaultObjectBinder(Key<TYPE> key) {
		this(key,new DefaultEnvironment());
	}
	
	
	public DefaultObjectBinder(Key<TYPE> key,Environment e) {
		
		super(key);
		env = e;
		
		//resolve key to implementation
		Kind<?> provided = env.resolver().resolve(key);
		
		//extract 'raw' type
		Class<?> clazz = null;
		switch (provided.value()) {
			case CLASS: //simply type-extract class itself
				clazz = CLASS(provided); 
				break;
			case GENERIC: //extract raw type and store variable bindings in the environment
				ParameterizedType pt = GENERIC(provided);
				clazz = (Class<?>) pt.getRawType();
				TypeVariable<?>[] vars = clazz.getTypeParameters(); 
				for (int i = 0; i<vars.length; i++)
					env.bindVariable(vars[i], env.binderFor(get(pt.getActualTypeArguments()[i])));
				break;
			default:
				throw new RuntimeException("unexpected kind "+provided);
		}
		
		if (clazz.isInterface()) 
			throw new RuntimeException("unexpected interface "+clazz);
		
		setConstructor(clazz);
		setMethods(clazz);
		
	}
	
	/**{@inheritDoc}*/
	public Map<QName,TypeBinder<?>> binders() {
		return binders;
	}
	
	/**{@inheritDoc}*/
	@Override
	public TYPE bind(List<Provided> provided) {
		try {
			
			if (provided.size()!=1)
				switch(mode()) {
					case STRICT:
							throw new RuntimeException("expected one value but found zero or many: "+provided);
					case LAX:
							return null;
				}
			
			
			DataProvider dp = provided.get(0).provider();
			
			if (!(dp instanceof StructureProvider))
				switch(mode()) {
					case STRICT:
						throw new RuntimeException("expected a structure but found: "+provided);
					case LAX:
						return null;
		}
			
			StructureProvider provider = (StructureProvider) dp;
			
			List<Object> vals = new LinkedList<Object>();
			
			//extract constructor parameters and off-load creation to factory
			for (QName name : constructorDef.names())
				vals.add(binders.get(name).bind(provider.get(name)));
				
			TYPE object = env.resolver().resolve(key(),vals);
		
			for (MethodDef m : methodDefs) {
				vals.clear();
				for (QName name : m.names()) {
					Object part = binders.get(name).bind(provider.get(name));
					vals.add(part);
				}
				
				m.method().invoke(object,vals.toArray(new Object[0]));				
			}
			
			return object;
		}
		catch(Throwable e) {
			throw new RuntimeException(format("cannot bind %1s to %2s",key(),provided),e);
		}
	}
	
	
	void setConstructor(Class<?> clazz) {
		
		List<QName> boundNames = new ArrayList<QName>();
		Constructor<?> constructor=null;
		
		Class<?> parent = clazz; 
		do 
			
			//identify constructor
			for (Constructor<?> c : parent.getDeclaredConstructors()) {
				boundNames = addBinder(parent,c.getParameterAnnotations(), c.getGenericParameterTypes());
				if (boundNames.size()>0) {
					if (constructor==null)
						constructor=c;
					else
						throw new RuntimeException(format("%1s has more than one bound constructor",clazz.getName()));
				}	
			
			}
		
		while //repeat for inherited methods
			((parent=parent.getSuperclass())!=null);
		
		//if no constructorDef is annotated, try to use nullary one
		if (constructor==null)
			try {
				constructor = clazz.getDeclaredConstructor();
			}
			catch(Throwable e) {
				throw new RuntimeException(format("%1s has no nullary or annotated constructors",clazz.getName()));
			
			}
		
		constructor.setAccessible(true);
		
		logger.trace("bound constructor for {} is {}",clazz.getName(),constructor.getName());
		
		constructorDef = new ConstructorDef(constructor,boundNames);
		
		
	}

	void setMethods(Class<?> clazz) {
		
		do 
		
			for (Method m : clazz.getDeclaredMethods()) {
				
				//skip methods that do not occur in source
				if (m.isSynthetic())
					continue;
						
				Type[] params = m.getGenericParameterTypes();
				List<QName> boundNames = addBinder(clazz,m.getParameterAnnotations(),params);
				
				//scan interfaces for possible annotations
				if (boundNames.isEmpty())
					for (Class<?> i : clazz.getInterfaces())
						try {
							//find methods by 'raw' type (interface could be parametric)
							Method overridden = i.getMethod(m.getName(),m.getParameterTypes());
							//but do use the resolved parameters
							boundNames = addBinder(clazz,overridden.getParameterAnnotations(),params);
						}
						catch(NoSuchMethodException e) {
							continue;
						}
		
				if (!boundNames.isEmpty()) {
					
					//no private methods
					if (Modifier.isPrivate(m.getModifiers()))
						throw new RuntimeException(format("cannot project over private method '%1s' in %2s",m.getName(),clazz.getName()));
					
					m.setAccessible(true);
					methodDefs.add(new MethodDef(m,boundNames));
					
					logger.trace("bound method '{}' for {}",m.getName(),clazz.getName());
				}
			}
		
		while //repeat for inherited methods
			((clazz=clazz.getSuperclass())!=null);
		
	}
	
	
	List<QName> addBinder(Class<?> clazz,Annotation[][] annotationLists, Type parameters[]) {
		
		List<QName> boundNames = new LinkedList<QName>();
		for (int i =0; i<parameters.length;i++)
			for (Annotation annotation : annotationLists[i])
				if (annotation instanceof Bind) {
					
					Bind bindAnnotation = (Bind) annotation;
					QName name = new QName(bindAnnotation.ns(),bindAnnotation.value());
					
					if (binders.containsKey(name))
						throw new RuntimeException(format("bound name %1s is duplicated in %2s",name,clazz.getName()));
					else {	
						
						boundNames.add(name);
						
						Key<?> key = get(parameters[i],qualifier(annotationLists[i]));
						
						TypeBinder<?> binder = env.binderFor(key);
						
						//set mode
						if (bindAnnotation.mode()!=DEFAULT)
							binder.setMode(bindAnnotation.mode());
						
						binders.put(name,binder);
					}
					break;
				}
		
		return boundNames;
	}
	
	Class<? extends Annotation> qualifier(Annotation[] annotations) {
		for (Annotation a : annotations)
			if (a.annotationType().isAnnotationPresent(Qualifier.class))
				return a.annotationType();
		return null;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return "obj"+binders;
	}
	
	
	public static class ObjectBinderProvider implements BinderProvider<Object> {

		/**{@inheritDoc}*/
		@Override
		public TypeBinder<Object> binder(Key<Object> key, Environment env) {
			return new DefaultObjectBinder<Object>(key, env);
		}
		
		/**{@inheritDoc}*/
		@Override
		public Key<Object> matchingKey() {
			return get(Object.class);
		}
		
	}
	
}
