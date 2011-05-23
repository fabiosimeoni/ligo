/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static java.lang.String.*;
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

import org.ligo.lab.data.Provided;
import org.ligo.lab.typebinders.Bind;
import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.TypeBinder;
import org.ligo.lab.typebinders.TypeBinderFactory;
import org.ligo.lab.typebinders.kinds.Kind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni
 *
 */
public class ClassBinder<TYPE> extends AbstractTypeBinder<TYPE> {

	private static final Logger logger = LoggerFactory.getLogger(ClassBinder.class);
	
	private final TypeBinderFactory factory;
	
	@SuppressWarnings("unused")
	private ConstructorDef constructorDef;
	private List<MethodDef> methodDefs = new LinkedList<MethodDef>();
	
	private Map<QName,TypeBinder<?>> parts = new HashMap<QName, TypeBinder<?>>();
	
	
	public ClassBinder(Key<TYPE> key,TypeBinderFactory f) {
		
		super(key);
		factory = f;
		
		//resolve key to implementation
		Kind<?> provided = factory.resolver().resolve(key);
		
		//extract 'raw' type
		Class<?> clazz = null;
		switch (provided.value()) {
			case CLASS:
				clazz = CLASS(provided); 
				break;
			case GENERIC:
				ParameterizedType pt = GENERIC(provided);
				clazz = (Class<?>) pt.getRawType();
				//push variable bindings
				TypeVariable<?>[] vars = clazz.getTypeParameters(); 
				for (int i = 0; i<vars.length; i++)
					factory.addVariable(vars[i], pt.getActualTypeArguments()[i]);
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
	public Map<QName,TypeBinder<?>> parts() {
		return parts;
	}
	
	/**{@inheritDoc}*/
	@Override
	public TYPE bind(List<Provided> in) {
		//TODO
		return null;
	}
	
	
	void setConstructor(Class<?> clazz) {
		
		List<QName> boundNames = new ArrayList<QName>();
		Constructor<?> constructor=null;
		//identify constructor
		for (Constructor<?> c : clazz.getDeclaredConstructors()) {
			boundNames = addParts(clazz,c.getParameterAnnotations(), c.getGenericParameterTypes());
			if (boundNames.size()>0) {
				if (constructor==null)
					constructor=c;
				else
					throw new RuntimeException(format("%1s has more than one bound constructor",clazz));
			}	
		
		}
		//if no constructorDef is annotated, try to use nullary one
		if (constructor==null)
			try {
				constructor = clazz.getDeclaredConstructor();
			}
			catch(Throwable e) {
				throw new RuntimeException(format("%1s has no nullary or annotated constructors",clazz));
			
			}
		
		constructor.setAccessible(true);
		
		logger.trace("bound constructor for {} is {}",clazz,constructor);
		
		constructorDef = new ConstructorDef(constructor,boundNames);
		
		
	}

	void setMethods(Class<?> clazz) {
		
		do 
		
			for (Method m : clazz.getDeclaredMethods()) {
				
				if (m.isSynthetic())
					continue;
						
				Type[] params = m.getGenericParameterTypes();
				List<QName> boundNames = addParts(clazz,m.getParameterAnnotations(),params);
				if (boundNames.isEmpty())
					//look in interfaces
					for (Class<?> i : clazz.getInterfaces())
						try {
							Method overridden = i.getMethod(m.getName(),m.getParameterTypes());
							boundNames = addParts(clazz,overridden.getParameterAnnotations(),params);
						}
						catch(NoSuchMethodException e) {
							continue;
						}
		
				if (!boundNames.isEmpty()) {
			
					if (Modifier.isPrivate(m.getModifiers()))
						throw new RuntimeException("cannot project over private method "+m);
					
					m.setAccessible(true);
					logger.trace("bound method {} for {}",m.getName(),clazz);
					methodDefs.add(new MethodDef(m,boundNames));
				}
			}
		
		while //repeat for methodDefs in superclass
			((clazz=clazz.getSuperclass())!=null);
		
	}
	
	
	List<QName> addParts(Class<?> clazz,Annotation[][] annotationLists, Type parameters[]) {
		
		List<QName> boundNames = new LinkedList<QName>();
		for (int i =0; i<parameters.length;i++)
			for (Annotation annotation : annotationLists[i])
				if (annotation instanceof Bind) {
					
					Bind bindAnnotation = (Bind) annotation;
					QName name = new QName(bindAnnotation.ns(),bindAnnotation.value());
					
					if (parts.containsKey(name))
						throw new RuntimeException(format("bound name %1s is duplicated in %2s",name,clazz));
					else {	
						boundNames.add(name);
						Key<?> key = get(parameters[i],getQualifier(annotationLists[i]));
						TypeBinder<?> binder = factory.binder(key);
						parts.put(name,binder);
					}
					break;
				}
		
		return boundNames;
	}
	
	Class<? extends Annotation> getQualifier(Annotation[] annotations) {
		for (Annotation a : annotations)
			if (a.annotationType().isAnnotationPresent(Qualifier.class))
				return a.annotationType();
		return null;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return "obj"+parts;
	}
	
	
	public static class DefaultClassBinderProvider<T> implements BinderProvider<T> {

		/**{@inheritDoc}*/
		@Override
		public TypeBinder<T> binder(Key<T> key, TypeBinderFactory factory) {
			return new ClassBinder<T>(key, factory);
		}
		
	}
}