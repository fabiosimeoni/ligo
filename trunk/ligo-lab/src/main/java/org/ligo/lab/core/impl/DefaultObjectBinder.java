/**
 * 
 */
package org.ligo.lab.core.impl;

import static java.lang.String.*;
import static java.util.Collections.*;
import static org.ligo.lab.core.Key.*;
import static org.ligo.lab.core.annotations.Bind.Mode.*;
import static org.ligo.lab.core.impl.ParameterContext.*;
import static org.ligo.lab.core.kinds.Kind.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.Key;
import org.ligo.lab.core.ObjectBinder;
import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.annotations.AnnotationProcessor;
import org.ligo.lab.core.annotations.Bind;
import org.ligo.lab.core.annotations.BindProcessor;
import org.ligo.lab.core.data.DataProvider;
import org.ligo.lab.core.data.Provided;
import org.ligo.lab.core.data.StructureProvider;
import org.ligo.lab.core.impl.AbstractMethodDef.NamedBinder;
import org.ligo.lab.core.kinds.Kind;
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
	private static final String MULTICONSTRUCTOR_ERROR= "%1s has more than one bound constructor";
	private static final String DUPLICATE_NAME= "bound name '%1s' is duplicated in %2s";
	private static final String NO_CONSTRUCTOR_ERROR="%1s has no nullary or annotated constructors";
	private static final String BOUND_CONSTRUCTOR_LOG="bound constructor {} for {}";
	private static String CARDINALITY_ERROR = "[%1s] binder for %2s required one value but found: %3s";
	private static String INPUT_ERROR = "[%1s] binder for %2s required a structure but found: %3s";
	
	private static Map<Class<? extends Annotation>,AnnotationProcessor> processors =
		new HashMap<Class<? extends Annotation>, AnnotationProcessor>();

	
	private static final QName UNBOUND_PARAM=new QName("__LIGO_UNBOUND__");
	
	
	static {
		processors.put(Bind.class, new BindProcessor());
	}
	
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
		
		//we do need class to be implementation
		if (clazz.isInterface()) 
			throw new RuntimeException(format(INTERFACE_ERROR,clazz));
		
		//analyse class
		bindConstructor(clazz);
		bindMethods(clazz);
		
		logger.trace(BUILT_LOG,this,mode());
	}
	
	void bindConstructor(Class<?> clazz) {
		
		//there must be only one annotated constructor
		//if this takes one parameter, then annotations can go on the constructor itself.
		//if it takes more, then annotations must go on parameters though not all parameters must have them.
		
		Constructor<?> constructor=null;
		
		List<NamedBinder> cbinders = new ArrayList<NamedBinder>();
		
		//identify constructor
		for (Constructor<?> c : clazz.getDeclaredConstructors()) {
			
			cbinders = addBinders(buildContexts(c));
			
			//remember and check uniqueness
			if (cbinders.size()>0) {
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
		constructorDef = new ConstructorDef(constructor,cbinders);
		
		
	}
	
	void bindMethods(Class<?> clazz) {
		
		do 
		
			for (Method m : clazz.getDeclaredMethods()) {
				
				//skip methods that do not occur in source
				if (m.isSynthetic())
					continue;
				
				List<NamedBinder> mbinders = addBinders(buildContexts(m));
				
				//scan interfaces for possible annotations
				if (mbinders.isEmpty())
					for (Class<?> i : clazz.getInterfaces())
						try {
							//find methods by 'raw' type (interface could be parametric)
							Method overridden = i.getMethod(m.getName(),m.getParameterTypes());
							//but do use the resolved parameters
							mbinders = addBinders(buildContexts(overridden,m.getGenericParameterTypes(), overridden.getParameterAnnotations()));
						}
						catch(NoSuchMethodException e) {
							continue;
						}
		
				if (!mbinders.isEmpty()) {
					
					//no private methods
					if (Modifier.isPrivate(m.getModifiers()))
						throw new RuntimeException(format("cannot bind private method '%1s' in %2s",m.getName(),clazz.getName()));
					
					m.setAccessible(true);
					
					methodDefs.add(new MethodDef(m,mbinders));
				}
			}
		
		while //repeat for inherited methods
			((clazz=clazz.getSuperclass())!=null);
		
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
			List<NamedBinder> cbinders = constructorDef.binders();
			for (NamedBinder named : cbinders)
				if (named.name.equals(UNBOUND_PARAM))
					vals.add(named.binder.bind(null));
				else
					try {
						vals.add(named.binder.bind(provider.get(named.name)));
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
				List<NamedBinder> mbinders = m.binders();
				for (NamedBinder named : mbinders) {
					if (named.name.equals(UNBOUND_PARAM))
						vals.add(named.binder.bind(null));
					else {
						Object part = named.binder.bind(provider.get(named.name));
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
	

	List<NamedBinder> addBinders(ParameterContext ... contexts) {
	
		//add binders for each context, checking they are unambiguously annotated.
		//these include constant binders for un-annotated parameters in otherwise annotated method
		
		Set<QName> boundNames = new HashSet<QName>(binders.keySet()); //bound so far, used for uniqueness
		
		List<NamedBinder> bound = new ArrayList<NamedBinder>(); //main output
		List<ConstantBinder> constants = new LinkedList<ConstantBinder>(); //keep track of unbound one
		
		for (ParameterContext context : contexts) {
			
			//bound context
			if (context.isBound()) {
				
				if (context.bindingAnnotation() instanceof Bind) {

					NamedBinder named = processors.get(Bind.class).binderFor(context,env);
					
					//check uniqueness
					if (boundNames.contains(named.name))
						throw new RuntimeException(format(DUPLICATE_NAME,named.name,context.member().getDeclaringClass().getName()));	
				
				//update state
				boundNames.add(named.name);
				bound.add(new NamedBinder(named.name,named.binder));
				
				//only bound binders are exposed.
				binders.put(named.name,named.binder);
				}
			}
			//unbound context
			else {
				ConstantBinder cbinder;
				try {
					cbinder = new ConstantBinder(context.type()); 
				}
				catch(final RuntimeException e) {
					cbinder = new ConstantBinder(Object.class) {
						public Object bind(List<Provided> i) {
							throw e;
						}
					};
				}
				
				constants.add(cbinder);
				bound.add(new NamedBinder(UNBOUND_PARAM,cbinder));
			}
			
		}

		//no parameters were bound
		if (constants.size()==bound.size())
			return emptyList();
		
		//test costant binders for early feedback
		for (ConstantBinder cbinder : constants)
			cbinder.bind(null);
		
		return bound;
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
