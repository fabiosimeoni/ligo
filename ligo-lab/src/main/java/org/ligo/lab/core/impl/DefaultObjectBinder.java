/**
 * 
 */
package org.ligo.lab.core.impl;

import static java.lang.String.*;
import static java.util.Collections.*;
import static org.ligo.lab.core.annotations.Bind.Mode.*;
import static org.ligo.lab.core.impl.ParameterContext.*;
import static org.ligo.lab.core.keys.Keys.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.ObjectBinder;
import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.annotations.AnnotationProcessor;
import org.ligo.lab.core.annotations.Bind;
import org.ligo.lab.core.annotations.BindProcessor;
import org.ligo.lab.core.data.DataProvider;
import org.ligo.lab.core.data.Provided;
import org.ligo.lab.core.data.StructureProvider;
import org.ligo.lab.core.keys.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link TypeBinder} for arbitrary object structures.
 * 
 * @author Fabio Simeoni
 *
 */
class DefaultObjectBinder<T> extends AbstractBinder<T> implements ObjectBinder<T> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultObjectBinder.class);
	
	static final String TO_STRING= "%1s-obj%2s";
	static final String MULTICONSTRUCTOR_ERROR= "%1s has more than one bound constructor";
	static final String DUPLICATE_NAME= "bound name '%1s' is duplicated in %2s";
	static final String NO_CONSTRUCTOR_ERROR="%1s has no nullary or annotated constructors";
	static String CARDINALITY_ERROR = "%1s required one value but found: %2s";
	static String INPUT_ERROR = "%1s required a structure but found: %2s";
	
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

	
	public DefaultObjectBinder(Key<? extends T> key, Environment e) {
		
		super(key);
		env = e;
		
		//analyse class
		bindConstructor(boundClass());
		bindMethods(boundClass());
		
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
		
		//logger.trace(BOUND_CONSTRUCTOR_LOG,clazz.getName(),constructor.getName());
		
		//remember bound names
		constructorDef = new ConstructorDef(constructor,cbinders);
		
		
	}
	
	void bindMethods(Class<?> clazz) {
		
		Map<String,Type[]> visitedMethods = new HashMap<String,Type[]>();
		
		do 
		
			for (Method m : clazz.getDeclaredMethods()) {
				
				//exclude overridden method
				if (Arrays.equals(m.getParameterTypes(),visitedMethods.get(m.getName())))
					continue;
				
				//mark visited method with its raw parameter types so as to detect synthetic overrides of generic types.
				visitedMethods.put(m.getName(),m.getParameterTypes());
			
				
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
//					if (Modifier.isPrivate(m.getModifiers()))
//						throw new RuntimeException(format("cannot bind private method '%1s' in %2s",m.getName(),clazz.getName()));
					
					m.setAccessible(true);
					
					methodDefs.add(new MethodDef(m,mbinders));
				}
			}
		
		while //repeat for inherited methods
			((clazz=clazz.getSuperclass())!=Object.class);
		
	}

	/**{@inheritDoc}*/
	public Map<QName,TypeBinder<?>> binders() {
		return new HashMap<QName, TypeBinder<?>>(binders);
	}
	
	/**{@inheritDoc}*/
	@Override
	public T bind(List<Provided> provided) {
		
		try {
			
			if (provided.size()!=1)
				if (mode()==STRICT)
					throw new RuntimeException(format(CARDINALITY_ERROR,this,provided));
				else
					return null;
			
			
			DataProvider dp = provided.get(0).provider();
			
			if (!(dp instanceof StructureProvider))
				if (mode()==STRICT)
					throw new RuntimeException(format(INPUT_ERROR,this,provided));
				else
					return null;
			
			StructureProvider provider = (StructureProvider) dp;
			
			//pull constructor parameters and off-load creation to env
			List<Object> values = extractvalues(constructorDef.binders(),provider);
			
			@SuppressWarnings("unchecked") //internally consistent
			T object = (T) env.resolver().resolve(key().kind().toClass(),values);
		
			//pull method parameters and invoke
			for (MethodDef m : methodDefs) {
				values = extractvalues(m.binders(),provider);
				m.method().invoke(object,values.toArray(new Object[0]));				
			}
			
			logger.trace(BINDING_SUCCESS_LOG,new Object[]{dp,this,object});
			
			return object;
		}
		catch(Throwable e) {
			throw new RuntimeException(format(BINDING_ERROR,key(),provided),e);
		}
	}
	
	List<Object> extractvalues(List<NamedBinder> binders, StructureProvider provider) {
		
		List<Object> values = new ArrayList<Object>();
		
		for (NamedBinder named : binders) {
			
			//set mode, lazily on potentially cached binders
			Bind bindAnnotation = (Bind) named.parameterContext().bindingAnnotation(); 
			if (bindAnnotation!=null && bindAnnotation.mode()!=DEFAULT)
				named.binder().setMode(bindAnnotation.mode());
			
			if (named.name().equals(UNBOUND_PARAM))
				values.add(named.binder().bind((Provided)null));
			else {
				Object part = named.binder().bind(provider.get(named.name()));
				values.add(part);
			}
		}
		return values;
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
					if (boundNames.contains(named.name()))
						throw new RuntimeException(format(DUPLICATE_NAME,named.name(),context.member().getDeclaringClass().getName()));	
				
				//update state
				boundNames.add(named.name());
				bound.add(new NamedBinder(named.name(),named.binder(),context));
				
				//only bound binders are exposed.
				binders.put(named.name(),named.binder());
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
				bound.add(new NamedBinder(UNBOUND_PARAM,cbinder,context));
			}
			
		}

		//no parameters were bound
		if (constants.size()==bound.size())
			return emptyList();
		
		//test costant binders for early feedback
		for (ConstantBinder cbinder : constants)
			cbinder.bind((Provided)null);
		
		return bound;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(TO_STRING,super.toString(),binders);
	}
	
	
	public static class ObjectBinderProvider implements BinderProvider<Object> {

		/**{@inheritDoc}*/
		@Override
		public Key<Object> matchingKey() {
			return newKey(Object.class);
		}

		/**{@inheritDoc}*/
		@Override
		public TypeBinder<Object> binder(Key<Object> key, Environment env) {
			return new DefaultObjectBinder<Object>(key,env);
		}
		
	}
	
}