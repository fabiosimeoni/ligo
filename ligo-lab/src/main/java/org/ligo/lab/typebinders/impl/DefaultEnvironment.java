/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static java.util.Arrays.*;
import static org.ligo.lab.typebinders.Key.*;
import static org.ligo.lab.typebinders.kinds.Kind.*;
import static org.ligo.lab.typebinders.kinds.Kind.KindValue.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.ligo.lab.typebinders.Environment;
import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.Resolver;
import org.ligo.lab.typebinders.TypeBinder;
import org.ligo.lab.typebinders.impl.DefaultIteratorBinder.IteratorBinderProvider;
import org.ligo.lab.typebinders.impl.DefaultObjectBinder.ObjectBinderProvider;
import org.ligo.lab.typebinders.impl.PrimitiveBinders.BooleanBinder;
import org.ligo.lab.typebinders.impl.PrimitiveBinders.DoubleBinder;
import org.ligo.lab.typebinders.impl.PrimitiveBinders.FloatBinder;
import org.ligo.lab.typebinders.impl.PrimitiveBinders.IntBinder;
import org.ligo.lab.typebinders.impl.PrimitiveBinders.StringBinder;
import org.ligo.lab.typebinders.kinds.Kind;
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
	
	private static Map<Key<?>,TypeBinder<?>> cache = new HashMap<Key<?>,TypeBinder<?>>();
	private static Map<Key<?>,BinderProvider<?>> providers = new HashMap<Key<?>,BinderProvider<?>>();
	
	@SuppressWarnings("unchecked")
	private static final List<BinderProvider<?>> DEFAULT_PROVIDERS = (List) asList(
			new ObjectBinderProvider(),
			DefaultCollectionBinder.provider(Collection.class),
			DefaultCollectionBinder.provider(List.class),
			DefaultCollectionBinder.provider(Set.class),
			DefaultCollectionBinder.provider(Queue.class),
			DefaultCollectionBinder.provider(Deque.class),
			new IteratorBinderProvider(),
			new StringBinder().provider(),
			new IntBinder().provider(),
			new FloatBinder().provider(),
			new DoubleBinder().provider(),
			new BooleanBinder().provider()
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
	@Override
	public <T> TypeBinder<T> binderFor(Key<T> key) {
		
		//hit cache
		@SuppressWarnings("unchecked") //internally consistent
		TypeBinder<T> binder = (TypeBinder) cache.get(key);
		
		if (binder!=null) {
			logger.trace("resolved {} from cache",key);
			return binder;
		}
		
		//resolve provider
		BinderProvider<?> provider = provider(key);
		
		if (provider==null)
			throw new RuntimeException("no provider available for "+key);
		
		@SuppressWarnings("unchecked") //internally consistent
		TypeBinder<T> newBinder = (TypeBinder) provider.binder((Key)key,this); 
		
		//update cache
		cache.put(key,newBinder);
		
		return newBinder;
		
	}

	/**
	 * Used internally to resolve {@link BinderProvider}s from {@link Key}s.
	 * @param key the key.
	 * @return the provider.
	 */
	BinderProvider<?> provider(Key<?> key) {
		
		BinderProvider<?> provider = providers.get(key);
		
		//try raw type
		
		Kind<?> kind = key.kind();
		if (provider==null && kind.value()==GENERIC)
			provider = providers.get(get(GENERIC(kind).getRawType(),key.qualifier()));
		
		//defaults to object
		if (provider==null)
			provider = providers.get(Object.class);
		
		return provider;
	}
	
	/**{@inheritDoc}*/
	@Override
	public void bindVariable(TypeVariable<?> var, TypeBinder<?> binder) {
		cache.put(get(var), binder);
	}
	
}
