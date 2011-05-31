/**
 * 
 */
package org.ligo.core.impl;

import static org.ligo.core.keys.Keys.*;
import static org.ligo.core.kinds.Kind.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

import org.ligo.core.CollectionBinder;
import org.ligo.core.Environment;
import org.ligo.core.IteratorBinder;
import org.ligo.core.TypeBinder;
import org.ligo.core.data.Provided;
import org.ligo.core.keys.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Fabio Simeoni
 *
 */
class DefaultIteratorBinder<TYPE> extends AbstractBinder<Iterator<TYPE>> implements IteratorBinder<TYPE> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultIteratorBinder.class);
	
	private final CollectionBinder<List<TYPE>,TYPE> backing;
	
	@SuppressWarnings("unchecked")
	DefaultIteratorBinder(final Key<? extends Iterator<TYPE>> key, Environment e) {
		
		super(key);
		
		ParameterizedType type = new ParameterizedType() {
			@Override public Type getRawType() {return List.class;}
			@Override public Type getOwnerType() {return null;}
			@Override public Type[] getActualTypeArguments() {return new Type[]{GENERIC(key.kind()).getActualTypeArguments()[0]};}
		};
		
		TypeBinder<List<TYPE>> binder = (CollectionBinder) (TypeBinder) e.binderFor(newKey(type,key.qualifier()));
		
		if (!(binder instanceof CollectionBinder<?,?>))
			throw new RuntimeException("Binding iterators requires binding collections, " +
							"but no collection binder was registered");
		
		backing= (CollectionBinder) binder;
		
	}
	
	@Override
	public TypeBinder<?> binder() {
		return backing.binder();
	}
	
	@Override
	public Iterator<TYPE> bind(List<Provided> provided) {
		Iterator<TYPE> iterator = backing.bind(provided).iterator();
		logger.trace("bound iterator {}",iterator);
		return iterator;
	}

	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return binder().toString();
	}
	
	public static class IteratorBinderProvider implements BinderProvider<Iterator<Object>> {

		/**{@inheritDoc}*/
		@Override
		public TypeBinder<Iterator<Object>> binder(Key<Iterator<Object>> key, Environment env) {
			return new DefaultIteratorBinder<Object>(key,env);
		}			
		
		@SuppressWarnings("unchecked")
		@Override public Key<Iterator<Object>> matchingKey() {
			return (Key) newKey(Iterator.class);
		}
			
	};
	
}
