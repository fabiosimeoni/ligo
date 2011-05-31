/**
 * 
 */
package org.ligo.core.impl;

import static java.lang.String.*;
import static java.util.Arrays.*;
import static org.ligo.core.keys.Keys.*;
import static org.ligo.core.kinds.Kind.*;
import static org.ligo.core.kinds.Kind.KindValue.*;
import static org.ligo.core.utils.ReflectionUtils.*;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ligo.core.Environment;
import org.ligo.core.Literal;
import org.ligo.core.Resolver;
import org.ligo.core.TypeBinder;
import org.ligo.core.keys.Key;
import org.ligo.core.kinds.Kind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The default {@link Environment} implementation.
 * <p>
 * It caches {@link TypeBinder}s and resolves type variables. By default, it relies on a {@link LigoResolver}
 * and on a set of default {@link BinderProvider}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class LigoEnvironment implements Environment {


	private static Logger logger = LoggerFactory.getLogger(LigoEnvironment.class);
	
	private static final String UNBOUND_VARIABLE_ERROR = "variable %1s is unbound";
	private static final String NO_PROVIDER_ERROR = "no provider available for %1s";
	private static final String INTERFACE_ERROR="%1s resolved into interface or abstract class %2s, a concrete implementation is required";
	private static final String BUILD_LOG = "building binder for {}";
	private static String BUILT_LOG = "built {} for {}";
	private static final String CACHE_HIT_LOG = "resolved {} for {} from cache";
	private static final String VAR_RESOLVE_LOG = "resolved {} to {}";
	private static final String VAR_BIND_LOG = "bound {} to {}";

	private Map<Key<?>,TypeBinder<?>> cache = new HashMap<Key<?>,TypeBinder<?>>();
	private Map<TypeVariable<?>,Key<?>> vars = new HashMap<TypeVariable<?>,Key<?>>();
	private Map<Key<?>,BinderProvider<?>> providers = new HashMap<Key<?>,BinderProvider<?>>();
	
	@SuppressWarnings("unchecked")
	private static final List<BinderProvider<?>> DEFAULT_PROVIDERS = (List) asList(
			DefaultObjectBinder.provider(),
			DefaultCollectionBinder.provider(Collection.class),
			DefaultIteratorBinder.provider(),
			DefaultArrayBinder.provider(),
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
	public LigoEnvironment() {
		this(new LigoResolver());
	}
	
	/**
	 * Creates an instance with a given {@link Resolver} and default {@link BinderProvider}s.
	 * @param r the resolver.
	 */
	public LigoEnvironment(Resolver r) {
		this(r,DEFAULT_PROVIDERS);
	}
	
	/**
	 * Creates an instance with a given {@link Resolver} and given {@link BinderProvider}s.
	 * @param r the resolver.
	 * @param ps the providers.
	 */
	public LigoEnvironment(Resolver r, List<? extends BinderProvider<?>> ps) {
		
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
	@Override
	public <T> TypeBinder<T> binderFor(Literal<? extends T> lit) {
		return binderFor(newKey(lit));
	}
	
	/**{@inheritDoc}*/
	@Override
	public <T> TypeBinder<T> binderFor(Class<? extends T> clazz) {
		return binderFor(newKey(clazz));
	}
	
	/**{@inheritDoc}*/
	@Override
	public <T> TypeBinder<T> binderFor(Key<? extends T> qualifiedKey) {
		
		
		Kind<?> kind = qualifiedKey.kind();
		
		//get rid of qualifiers for lookup purposes (but keep parameters)
		Key<?> unqualifiedKey =qualifiedKey.unqualified();
		
		//resolve variables
		if (kind.value()==TYPEVAR) {
			TypeVariable<?> var = TYPEVAR(kind);
			@SuppressWarnings("unchecked") //internally consistent
			TypeBinder<T> binder = (TypeBinder) resolve(var);
			if (binder==null)
				throw new RuntimeException(String.format(UNBOUND_VARIABLE_ERROR,kind));
			logger.trace(VAR_RESOLVE_LOG,kind,binder);
			return binder;
		}
		//hit cache
		@SuppressWarnings("unchecked") //internally consistent
		TypeBinder<T> binder = (TypeBinder) cache.get(unqualifiedKey);
		
		if (binder!=null) {
			logger.trace(CACHE_HIT_LOG,binder,unqualifiedKey);
			return binder;
		}
		
		logger.trace(BUILD_LOG,unqualifiedKey);
		
		//find provider
		BinderProvider<?> provider = providerFor(unqualifiedKey);
		
		if (provider==null)
			throw new RuntimeException(String.format(NO_PROVIDER_ERROR,unqualifiedKey));
		
		
		List<? extends Class<?>> resolvedClasses = resolver.resolve(qualifiedKey);
		
		List<TypeBinder<T>> binders = new LinkedList<TypeBinder<T>>();
		
		for (final Class<?> resolvedClass : resolvedClasses) {
			
			if (isAbstract(resolvedClass))
				throw new RuntimeException(format(INTERFACE_ERROR,qualifiedKey,resolvedClass));
			
			Key<?> resolvedKey = resolveKey(qualifiedKey, resolvedClass);
			
			//bind variables
			bindVariables(resolvedKey.kind());
			
			@SuppressWarnings("unchecked") //internally consistent
			TypeBinder<T> newBinder = (TypeBinder) provider.binder((Key)resolvedKey,this); 
			
			logger.trace(BUILT_LOG,newBinder,qualifiedKey);
			
			//update cache
			cache.put(unqualifiedKey,newBinder);
			
			binders.add(newBinder);
		}
		
		
		return binders.size()==1?binders.get(0):new DefaultUnionBinder<T>(qualifiedKey,this, binders);
		
	}
	
	Key<?> resolveKey(final Key<?> key, final Class<?> clazz) {
		
		Kind<?> resolvedKind;
		
		//for correct variable resolution we build a generic 
		//out of the implementation and the original type params (e.g. ArrayList<E> from resolved List<E>)
		switch(key.kind().value()) {
			case GENERIC:
				resolvedKind = kindOf(new ParameterizedType() {
					@Override public Type getRawType() {return clazz;}
					@Override public Type getOwnerType() {return null;}
					@Override public Type[] getActualTypeArguments() {return GENERIC(key.kind()).getActualTypeArguments();}
				});
				break;
			case GENERICARRAY:
				resolvedKind=key.kind();
				break;
			default:
				resolvedKind = kindOf(clazz);	
		}
		
		return newKey(resolvedKind,key.qualifier());
		
	}
	
	boolean isAbstract(Class<?> clazz) {
		
		//sanity check: we do need resolved class to be a concrete implementation
		return (clazz.isInterface() || (!clazz.isPrimitive() && !clazz.isArray() && Modifier.isAbstract(clazz.getModifiers()))); 
			
	}
	
	/**
	 * Used internally to resolve {@link BinderProvider}s from {@link Key}s.
	 * @param key the key.
	 * @return the provider.
	 */
	BinderProvider<?> providerFor(Key<?> key) {
		
		BinderProvider<?> provider = providers.get(key);
		
		if (provider==null) {
			
				
			//reduce search space to 'raw' types
			Class<?> clazz = key.toClass();
			if (clazz==null)
				return null;
		
			//lookup raw type
			provider = providers.get(newKey(clazz));
			
			if (clazz.isArray()) {
				provider=providers.get(newKey(Object[].class));
			}
			
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
	
	void bindVariables(Kind<?> kind) {
		
		Class<?> clazz = kind.toClass();
		
		if (clazz==Object.class)
			return;
		
		if (kind.value()==GENERIC) {
			TypeVariable<?>[] typevars = clazz.getTypeParameters(); 
			for (int i = 0; i<typevars.length; i++) { 
				Key<?> key = newKey(GENERIC(kind).getActualTypeArguments()[i]);
				vars.put(typevars[i],key);
				logger.trace(VAR_BIND_LOG,kindOf(typevars[i]),key);
			}
		}
		
		Type supertype = clazz.getGenericSuperclass();
		
		if (supertype!=null)
			bindVariables(kindOf(supertype));
	}
	
	TypeBinder<?> resolve(TypeVariable<?> var) {
		
		Key<?> key = vars.get(var);
		
		if (key==null)
			return null;
		
		//recurse over variable chains and return binder at end of chain
		if (key.kind().value()==TYPEVAR)
			return resolve(TYPEVAR(key.kind()));
		else
			return binderFor(key);
	}
}
