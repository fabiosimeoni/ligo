/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static org.ligo.lab.typebinders.Key.*;
import static org.ligo.lab.typebinders.kinds.Kind.*;
import static org.ligo.lab.typebinders.kinds.Kind.KindValue.*;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.ligo.lab.typebinders.TypeResolver;
import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.TypeBinder;
import org.ligo.lab.typebinders.Environment;
import org.ligo.lab.typebinders.kinds.Kind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Fabio Simeoni
 *
 */
public class DefaultEnvironment implements Environment {

	private static Logger logger = LoggerFactory.getLogger(DefaultEnvironment.class);
	
	private static Map<Key<?>,TypeBinder<?>> cache = new HashMap<Key<?>,TypeBinder<?>>();
	private static Map<Key<?>,BinderProvider<?>> providers = new HashMap<Key<?>,BinderProvider<?>>();
	
	private final TypeResolver implProvider;
	
	/**
	 * 
	 */
	public DefaultEnvironment(TypeResolver p) {
		implProvider = p;
	}
	
	/**{@inheritDoc}*/
	@Override
	public TypeResolver resolver() {
		return implProvider;
	}
	
	public <T> void addBinderProvider(Key<T> key,BinderProvider<T> provider) {
		
		providers.put(key,provider);

	}
	
	/**{@inheritDoc}*/
	@Override
	public <T> TypeBinder<T> binder(Key<T> key) {
		
		//hit cache
		@SuppressWarnings("unchecked") //internally consistent
		TypeBinder<T> binder = (TypeBinder) cache.get(key);
		
		if (binder!=null) {
			logger.trace("resolved {} from cache",key);
			return binder;
		}
		
		//resolve provider
		BinderProvider<?> provider = getProvider(key);
		
		if (provider==null)
			throw new RuntimeException("no provider available for "+key);
		
		@SuppressWarnings("unchecked") //internally consistent
		TypeBinder<T> newBinder = (TypeBinder) provider.binder((Key)key,this); 
		
		//update cache
		cache.put(key,newBinder);
		
		return newBinder;
		
	}

	BinderProvider<?> getProvider(Key<?> key) {
		
		BinderProvider<?> provider = providers.get(key);
		
		//try raw type
		Kind<?> kind = key.kind();
		if (provider==null && kind.value()==GENERIC) {
			provider = providers.get(get(GENERIC(kind).getRawType(),key.qualifier()));
		}

		//default to object
		if (provider==null)
			provider = providers.get(Object.class);
		
		return provider;
	}
	
	/**{@inheritDoc}*/
	@Override
	public void addVariable(TypeVariable<?> var, Type type) {
		cache.put(get(var), binder(get(type)));
	}
	
}
