/**
 * 
 */
package org.ligo.lab.core.impl;

import static java.util.Arrays.*;
import static org.ligo.lab.core.keys.Keys.*;
import static org.ligo.lab.core.kinds.Kind.*;
import static org.ligo.lab.core.kinds.Kind.KindValue.*;
import static org.ligo.lab.core.utils.ReflectionUtils.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.Resolver;
import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.impl.DefaultObjectBinder.ObjectBinderProvider;
import org.ligo.lab.core.keys.ClassKey;
import org.ligo.lab.core.keys.Key;
import org.ligo.lab.core.kinds.Kind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A default, caching binding {@link Environment}.
 * 
 * @author Fabio Simeoni
 *
 */
public class DefaultEnvironment implements Environment {


	private static Logger logger = LoggerFactory.getLogger(DefaultEnvironment.class);
	
	private static final String UNBOUND_VARIABLE_ERROR = "variable %1s is unbound";
	private static final String NO_PROVIDER_ERROR = "no provider available for %1s";
	private static final String RESOLVE_LOG = "resolving binder for {}";
	private static final String CACHE_HIT_LOG = "resolved binder {} from cache";
	private static final String VAR_RESOLVE_LOG = "resolved {} to {}";
	private static final String VAR_BIND_LOG = "binding {} to {}";

	private static Map<Key<?>,TypeBinder<?>> cache = new HashMap<Key<?>,TypeBinder<?>>();
	private static Map<TypeVariable<?>,Stack<TypeBinder<?>>> vars = new HashMap<TypeVariable<?>,Stack<TypeBinder<?>>>();
	private static Map<Key<?>,BinderProvider<?>> providers = new HashMap<Key<?>,BinderProvider<?>>();
	
	@SuppressWarnings("unchecked")
	private static final List<BinderProvider<?>> DEFAULT_PROVIDERS = (List) asList(
			new ObjectBinderProvider(),
			DefaultCollectionBinder.provider(Collection.class),
			PrimitiveBinder.provider(String.class),
			PrimitiveBinder.provider(Byte.class),
			PrimitiveBinder.provider(Short.class),
			PrimitiveBinder.provider(Integer.class),
			PrimitiveBinder.provider(Long.class),
			PrimitiveBinder.provider(Float.class),
			PrimitiveBinder.provider(Double.class),
			PrimitiveBinder.provider(Character.class),
			PrimitiveBinder.provider(Boolean.class)
	);

	
	private final Resolver resolver;
	
	/**
	 * Creates an instance with default dependencies.
	 */
	public DefaultEnvironment() {
		this(new DefaultResolver());
	}
	
	/**
	 * Creates an instance with a given {@link Resolver} and default {@link BinderProvider}s.
	 * @param r the resolver.
	 */
	public DefaultEnvironment(Resolver r) {
		this(r,DEFAULT_PROVIDERS);
	}
	
	/**
	 * Creates an instance with a given {@link Resolver} and given {@link BinderProvider}s.
	 * @param r the resolver.
	 * @param ps the providers.
	 */
	public DefaultEnvironment(Resolver r, List<? extends BinderProvider<?>> ps) {
		resolver = r;
		//load providers
		for (BinderProvider<?> provider : ps)
			providers.put(provider.matchingKey(), provider);
	}
	
	/**{@inheritDoc}*/
	@Override
	public Resolver resolver() {
		return resolver;
	}
	
	/**{@inheritDoc}*/
	public <T> TypeBinder<T> binderFor(Class<? extends T> clazz) {
		return binderFor(newKey(clazz));
	}
	
	/**{@inheritDoc}*/
	@Override
	public <T> TypeBinder<T> binderFor(Key<T> qualifiedKey) {
		
		Kind<?> kind = qualifiedKey.kind();
		
		//get rid of qualifiers for lookup purposes (but keep parameters)
		Key<?> unqualifiedKey =qualifiedKey.unqualified();
		
		logger.trace(RESOLVE_LOG,unqualifiedKey);
		
		//resolve variables
		if (kind.value()==TYPEVAR) {
			TypeVariable<?> var = TYPEVAR(kind);
			@SuppressWarnings("unchecked") //internally consistent
			TypeBinder<T> binder = (TypeBinder) resolve(var);
			if (binder==null)
				throw new RuntimeException(String.format(UNBOUND_VARIABLE_ERROR,var));
			return binder;
		}
		//hit cache
		@SuppressWarnings("unchecked") //internally consistent
		TypeBinder<T> binder = (TypeBinder) cache.get(unqualifiedKey);
		
		if (binder!=null) {
			logger.trace(CACHE_HIT_LOG,unqualifiedKey);
			return binder;
		}
		
		//find provider
		BinderProvider<?> provider = provider(unqualifiedKey);
		
		if (provider==null)
			throw new RuntimeException(String.format(NO_PROVIDER_ERROR,unqualifiedKey));
		
		//bind type variables, if parametric
		if (kind.value()==GENERIC) {
			TypeVariable<?>[] vars = kind.toClass().getTypeParameters(); 
			for (int i = 0; i<vars.length; i++)
				bind(vars[i], binderFor(newKey(GENERIC(kind).getActualTypeArguments()[i])));
		}
		
		//resolve to implementations (using parameters and annotations)
		Class<T> resolvedClass = resolver.resolve(qualifiedKey).get(0);
		
		@SuppressWarnings("unchecked") //internally consistent
		TypeBinder<T> newBinder = (TypeBinder) provider.binder((ClassKey)newKey(resolvedClass,qualifiedKey.qualifier()),this); 
		
		//update cache
		cache.put(unqualifiedKey,newBinder);
		
		return newBinder;
		
	}

	/**
	 * Used internally to resolve {@link BinderProvider}s from {@link Key}s.
	 * @param key the key.
	 * @return the provider.
	 */
	BinderProvider<?> provider(Key<?> key) {
		
		BinderProvider<?> provider = providers.get(key);
		
		if (provider==null) {
			
			//reduce search space to 'raw' types
			Class<?> clazz = key.kind().toClass();
			if (clazz==null)
				return null;
		
			//lookup raw type
			provider = providers.get(newKey(clazz));
			
			if (provider==null && Collection.class.isAssignableFrom(clazz))
				provider=providers.get(newKey(Collection.class));

			if (provider==null && Iterator.class.isAssignableFrom(clazz))
				provider=providers.get(newKey(Iterator.class));
			
			if (provider==null && clazz.isPrimitive())
				provider = providers.get(newKey(wrapperOf(clazz)));	
					
			//defaults to object
			if (provider==null)
				provider = providers.get(newKey(Object.class));
		}
		
		return provider;
	}
	
	void bind(TypeVariable<?> var, TypeBinder<?> binder) {
		logger.trace(VAR_BIND_LOG,var,binder);
		Stack<TypeBinder<?>> binders = vars.get(var);
		if (binders==null) {
			binders = new Stack<TypeBinder<?>>();
			vars.put(var,binders);
		}
		binders.push(binder);
	}
	
	TypeBinder<?> resolve(TypeVariable<?> var) {
		Stack<TypeBinder<?>> binders = vars.get(var);
		TypeBinder<?> binder = null;
		if (binders!=null) {
			binder = binders.pop();
			logger.trace(VAR_RESOLVE_LOG,var,binder);
		}
		return binder;
	}

}
