/**
 * 
 */
package org.ligo.core.impl;

import static java.lang.String.*;
import static java.util.Collections.*;
import static org.ligo.core.annotations.Bind.Mode.*;
import static org.ligo.core.impl.ParameterContext.*;
import static org.ligo.core.keys.Keys.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
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

import org.ligo.core.Environment;
import org.ligo.core.ObjectBinder;
import org.ligo.core.TypeBinder;
import org.ligo.core.annotations.AnnotationProcessor;
import org.ligo.core.annotations.Bind;
import org.ligo.core.annotations.BindProcessor;
import org.ligo.core.data.DataProvider;
import org.ligo.core.data.Provided;
import org.ligo.core.data.StructureProvider;
import org.ligo.core.keys.Key;
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
	
	private final BoundConstructor boundConstructor;
	private final List<BoundMethod> boundMethods;
	
	private Map<QName,TypeBinder<?>> binders = new HashMap<QName, TypeBinder<?>>();

	
	public DefaultObjectBinder(Key<? extends T> key, Environment e) {
		
		super(key);
		env = e;
		
		//analyse class
		boundConstructor = getBoundConstructor(boundClass());
		boundMethods = getBoundMethods(boundClass());
		
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
			List<Object> values = extractvalues(boundConstructor.parameterBinders(),provider);
			
			@SuppressWarnings("unchecked") //internally consistent
			T object = (T) env.resolver().resolve(key().kind().toClass(),values);
		
			//pull method parameters and invoke
			for (BoundMethod m : boundMethods) {
				values = extractvalues(m.parameterBinders(),provider);
				m.method().invoke(object,values.toArray(new Object[0]));				
			}
			
			logger.trace(BINDING_SUCCESS_LOG,new Object[]{dp,this,object});
			
			return object;
		}
		catch(Throwable e) {
			throw new RuntimeException(format(BINDING_ERROR,key(),provided),e);
		}
	}
	
	List<Object> extractvalues(List<? extends ParameterBinder<?>> binders, StructureProvider provider) {
		
		List<Object> values = new ArrayList<Object>();
		
		for (ParameterBinder<?> named : binders) {
			
			//set mode, lazily on potentially cached binders
			Bind bindAnnotation = (Bind) named.parameterContext().bindingAnnotation(); 
			if (bindAnnotation!=null && bindAnnotation.mode()!=DEFAULT)
				named.binder().setMode(bindAnnotation.mode());
			
			if (named.boundName().equals(UNBOUND_PARAM))
				values.add(named.binder().bind((Provided)null));
			else {
				Object part = named.binder().bind(provider.get(named.boundName()));
				values.add(part);
			}
		}
		return values;
	}
	
	BoundConstructor getBoundConstructor(Class<?> clazz) {
		
		//there must be only one annotated constructor
		//if this takes one parameter, then annotations can go on the constructor itself.
		//if it takes more, then annotations must go on parameters though not all parameters must have them.
		
		Constructor<?> constructor=null;
		
		List<ParameterBinder<Constructor<?>>> pbinders = new ArrayList<ParameterBinder<Constructor<?>>>();
		
		//identify constructor
		for (Constructor<?> c : clazz.getDeclaredConstructors()) {
			
			pbinders = addParameterBinders(buildContexts(c));
			
			//remember and check uniqueness
			if (pbinders.size()>0) {
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
		
		return new BoundConstructor(constructor,pbinders);
		
	}
	
	List<BoundMethod> getBoundMethods(Class<?> clazz) {
		
		List<BoundMethod> boundMethods = new ArrayList<BoundMethod>();
		Map<String,Type[]> visitedMethods = new HashMap<String,Type[]>();
		
		do 
		
			for (Method m : clazz.getDeclaredMethods()) {
				
				//exclude overridden method
				if (Arrays.equals(m.getParameterTypes(),visitedMethods.get(m.getName())))
					continue;
				
				//mark visited method with its raw parameter types so as to detect synthetic overrides of generic types.
				visitedMethods.put(m.getName(),m.getParameterTypes());
			
				
				List<ParameterBinder<Method>> pbinders = addParameterBinders(buildContexts(m));
				
				//scan interfaces for possible annotations
				if (pbinders.isEmpty())
					for (Class<?> i : clazz.getInterfaces())
						try {
							//find methods by 'raw' type (interface could be parametric)
							Method overridden = i.getMethod(m.getName(),m.getParameterTypes());
							//but do use the resolved parameters
							pbinders = addParameterBinders(buildContexts(overridden,m.getGenericParameterTypes(), overridden.getParameterAnnotations()));
						}
						catch(NoSuchMethodException e) {
							continue;
						}
		
				if (!pbinders.isEmpty()) {
					
					//no private methods
//					if (Modifier.isPrivate(m.getModifiers()))
//						throw new RuntimeException(format("cannot bind private method '%1s' in %2s",m.getName(),clazz.getName()));
					
					m.setAccessible(true);
					
					boundMethods.add(new BoundMethod(m,pbinders));
				}
			}
		
		while //repeat for inherited methods
			((clazz=clazz.getSuperclass())!=Object.class);
		
		return boundMethods;
		
	}
	
	<M extends Member> List<ParameterBinder<M>> addParameterBinders(List<ParameterContext<M>> contexts) {
	
		//add binders for each context, checking they are unambiguously annotated.
		//these include constant binders for un-annotated parameters in otherwise annotated method
		
		Set<QName> boundNames = new HashSet<QName>(binders.keySet()); //bound so far, used for uniqueness
		
		List<ParameterBinder<M>> bound = new ArrayList<ParameterBinder<M>>(); //main output
		List<ConstantBinder> constants = new LinkedList<ConstantBinder>(); //keep track of unbound one
		
		for (ParameterContext<M> context : contexts) {
			
			//bound context
			if (context.isBound()) {
				
				if (context.bindingAnnotation() instanceof Bind) {

					ParameterBinder<M> pbinder = processors.get(Bind.class).binderFor(context,env);
					
					//check uniqueness
					if (boundNames.contains(pbinder.boundName()))
						throw new RuntimeException(format(DUPLICATE_NAME,pbinder.boundName(),context.member().getDeclaringClass().getName()));	
				
				//update state
				boundNames.add(pbinder.boundName());
				bound.add(new ParameterBinder<M>(pbinder.boundName(),pbinder.binder(),context));
				
				//only bound binders are exposed.
				binders.put(pbinder.boundName(),pbinder.binder());
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
				bound.add(new ParameterBinder<M>(UNBOUND_PARAM,cbinder,context));
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
