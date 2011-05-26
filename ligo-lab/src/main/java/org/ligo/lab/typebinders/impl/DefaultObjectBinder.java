/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static java.lang.String.*;
import static java.util.Collections.*;
import static org.ligo.lab.typebinders.Bind.Mode.*;
import static org.ligo.lab.typebinders.Key.*;
import static org.ligo.lab.typebinders.impl.DefaultObjectBinder.ParameterContext.*;
import static org.ligo.lab.typebinders.kinds.Kind.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
	
	private static final String KIND_ERROR="unexpected type %1s";
	private static final String INTERFACE_ERROR="unexpected interface %1s";
	private static final String BINDMETHOD_ERROR= 
		"@Bind is allowed only on single-type constructors/methods, this is not the case for %1s, annotate individual parameters instead";
	private static final String MULTICONSTRUCTOR_ERROR= "%1s has more than one bound constructor";
	private static final String DUPLICATE_NAME= "bound name '%1s' is duplicated in %2s";
	private static final String NO_CONSTRUCTOR_ERROR="%1s has no nullary or annotated constructors";
	private static final String BOUND_CONSTRUCTOR_LOG="bound constructor {} for {}";
	private static String CARDINALITY_ERROR = "[%1s] binder for %2s required one value but found: %3s";
	private static String INPUT_ERROR = "[%1s] binder for %2s required a structure but found: %3s";
	
	private static final QName UNBOUND_PARAM=new QName("__LIGO_UNBOUND__");
	
		private final Environment env;
	
	private ConstructorDef constructorDef;
	private List<MethodDef> methodDefs = new LinkedList<MethodDef>();
	
	private Map<QName,TypeBinder<?>> binders = new HashMap<QName, TypeBinder<?>>();
	
	
	public DefaultObjectBinder(Key<? extends TYPE> key) {
		this(key,new DefaultEnvironment());
	}
	
	
	public DefaultObjectBinder(Key<? extends TYPE> key,Environment e) {
		
		super(key);
		env = e;
		
		//resolve key, as we need to analyse an implementation
		Kind<?> resolved = env.resolver().resolve(key);
		
		//extract class from kind
		Class<?> clazz;
		switch (resolved.value()) {
			case GENERIC:
				clazz = resolved.toClass();
				//bind type variables
				TypeVariable<?>[] vars = clazz.getTypeParameters(); 
				for (int i = 0; i<vars.length; i++)
					env.bindVariable(vars[i], env.binderFor(get(GENERIC(resolved).getActualTypeArguments()[i])));
			case CLASS: 
				clazz = CLASS(resolved); 
				break;
			default: //not other kinds can be processed by this binder
				throw new RuntimeException(format(KIND_ERROR,resolved));
		}
		
		//we do need class to be  implementation
		if (clazz.isInterface()) 
			throw new RuntimeException(format(INTERFACE_ERROR,clazz));
		
		//analyse
		setConstructor(clazz);
		setMethods(clazz);
		
		logger.trace(BUILT_LOG,this,mode());
	}
	
	void setConstructor(Class<?> clazz) {
		
		List<QName> boundNames = new ArrayList<QName>();
		Constructor<?> constructor=null;
		
		//identify constructor
		for (Constructor<?> c : clazz.getDeclaredConstructors()) {
			
			Type[] params = c.getGenericParameterTypes();
			
			//@Bind on constructor or @Bind on params?
			Bind bindAnnotation = c.getAnnotation(Bind.class);
			if (bindAnnotation==null) //@Bind on parameters 
				boundNames = addBinder(buildContexts(clazz, c, params, c.getParameterAnnotations()));
			else //@Bind on constructor
				if (params.length!=1) //validate
					throw new RuntimeException(format(BINDMETHOD_ERROR,c));
				else
					boundNames = addBinder(new ParameterContext(clazz,c,params[0],c.getAnnotations()));
				
				
			//remember and check uniqueness
			if (boundNames.size()>0) {
				if (constructor==null)
					constructor=c;
				else
					throw new RuntimeException(format(MULTICONSTRUCTOR_ERROR,clazz.getName()));
			}	
		
		}
		
		//no constructor yet, use nullary one
		if (constructor==null)
			try {
				constructor = clazz.getDeclaredConstructor();
			}
			catch(Throwable e) {
				throw new RuntimeException(format(NO_CONSTRUCTOR_ERROR,clazz.getName()));
			}
		
		//prep later access
		constructor.setAccessible(true);
		
		logger.trace(BOUND_CONSTRUCTOR_LOG,clazz.getName(),constructor.getName());
		
		//remember bound names
		constructorDef = new ConstructorDef(constructor,boundNames);
		
		
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
						throw new RuntimeException(format(CARDINALITY_ERROR, mode(),this,provided));
					case LAX:
							return null;
				}
			
			
			DataProvider dp = provided.get(0).provider();
			
			if (!(dp instanceof StructureProvider))
				switch(mode()) {
					case STRICT:
						throw new RuntimeException(format(INPUT_ERROR,mode(),this,provided));
					case LAX:
						return null;
			}
			
			StructureProvider provider = (StructureProvider) dp;
			
			List<Object> vals = new LinkedList<Object>();
			
			//pull constructor parameters and off-load creation to factory
			for (QName name : constructorDef.names())
				if (name.equals(UNBOUND_PARAM))
					vals.add(null);
				else
					try {
						vals.add(binders.get(name).bind(provider.get(name)));
					}
					catch(Throwable t) {
						if (mode()==STRICT)
							throw t;
						else
							vals.add(null);
					}
				
			TYPE object = env.resolver().resolve(key(),vals);
		
			for (MethodDef m : methodDefs) {
				vals.clear();
				for (QName name : m.names()) {
					if (name.equals(UNBOUND_PARAM))
						vals.add(null);
					else {
						Object part = binders.get(name).bind(provider.get(name));
						vals.add(part);
					}
				}
				
				m.method().invoke(object,vals.toArray(new Object[0]));				
			}
			
			logger.trace(BINDING_SUCCESS_LOG,new Object[]{mode(),dp,this,object});
			return object;
		}
		catch(Throwable e) {
			throw new RuntimeException(format(BINDING_ERROR,mode(),key(),provided),e);
		}
	}
	
	
	

	void setMethods(Class<?> clazz) {
		
		do 
		
			for (Method m : clazz.getDeclaredMethods()) {
				
				//skip methods that do not occur in source
				if (m.isSynthetic())
					continue;
				
				Type[] params = m.getGenericParameterTypes();
				List<QName> boundNames = null;
				
				//@Bind on constructor or @Bind on params?
				Bind bindAnnotation = m.getAnnotation(Bind.class);
				if (bindAnnotation==null) //@Bind on parameters 
					boundNames = addBinder(buildContexts(clazz, m, params, m.getParameterAnnotations()));
				else //@Bind on constructor
					if (params.length!=1) //validate
						throw new RuntimeException(format(BINDMETHOD_ERROR,m));
					else
						boundNames = addBinder(new ParameterContext(clazz,m,params[0],m.getAnnotations()));
				
				//scan interfaces for possible annotations
				if (boundNames.isEmpty())
					for (Class<?> i : clazz.getInterfaces())
						try {
							//find methods by 'raw' type (interface could be parametric)
							Method overridden = i.getMethod(m.getName(),m.getParameterTypes());
							//but do use the resolved parameters
							boundNames = addBinder(buildContexts(clazz, overridden, params, overridden.getParameterAnnotations()));
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
				}
			}
		
		while //repeat for inherited methods
			((clazz=clazz.getSuperclass())!=null);
		
	}
	
	static class ParameterContext {
		final Class<?> clazz;
		final Member member;
		final Type type;
		final Annotation[] annotations;
		Bind bindingAnnotation;
		Qualifier qualifier;
		ParameterContext(Class<?> c, Member m, Type p, Annotation[] as) {
			clazz=c; member=m; type=p; annotations=as;
			for (Annotation a : as)
				if (a instanceof Bind)
					if (bindingAnnotation==null)
						bindingAnnotation= (Bind) a;
					else
						throw new RuntimeException(format("mulitple binding annotations on %1s",member));
				else
					if (a instanceof Qualifier)
						if (qualifier ==null)
							qualifier = (Qualifier) a;
						else
							throw new RuntimeException(format("mulitple qualifiers on %1s",member));
		}
		boolean isBound() {
			return bindingAnnotation!=null;
		}
		
		QName boundName() {
			return bindingAnnotation==null?UNBOUND_PARAM:new QName(bindingAnnotation.ns(),bindingAnnotation.value());
		}
		
		Key<?> key() {
			return qualifier==null?get(type):get(type,qualifier.annotationType());
		}

		static ParameterContext[] buildContexts(Class<?> clazz, Member m, Type[] types, Annotation[][] as) {
			List<ParameterContext> contexts = new ArrayList<ParameterContext>();
			for (int i=0; i<types.length;i++)
				contexts.add(new ParameterContext(clazz, m, types[i], as[i]));
			return contexts.toArray(new ParameterContext[0]);
		}
	}
	
	List<QName> addBinder(ParameterContext ... contexts) {
		
		List<QName> boundNames = new LinkedList<QName>();
		List<ParameterContext> unbound = new LinkedList<ParameterContext>();
		
		for (ParameterContext context : contexts) {
			
			//bound type
			if (context.isBound()) {
				
				QName name = context.boundName();
				
				//uniqueness check
				if (boundNames.contains(name))
					throw new RuntimeException(format(DUPLICATE_NAME,name,context.clazz.getName()));
				
				binders.put(name,binderFor(context));
				boundNames.add(name);
					
			}
			//bound type
			else {
				unbound.add(context);
				boundNames.add(UNBOUND_PARAM);
			}
			
		}
		
		//no parameters were bound
		if (unbound.size()==boundNames.size())
			return emptyList();
		
		//process unbound parameters
		for (ParameterContext context : unbound) {
			Class<?> clazz = kindOf(context.type).toClass();
			if (clazz==null || clazz.isPrimitive())
				throw new RuntimeException(format("cannot bind constant to %1s",clazz));
		}
		
		return boundNames;
	}
	
	TypeBinder<?> binderFor(ParameterContext context) {
		
		//recur to obtain binder for type
		TypeBinder<?> binder = env.binderFor(context.key());
		
		//set mode
		if (context.bindingAnnotation.mode()!=DEFAULT)
			binder.setMode(context.bindingAnnotation.mode());	
		
		return binder;
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
