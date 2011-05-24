/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static org.ligo.lab.typebinders.Key.*;
import static org.ligo.lab.typebinders.kinds.Kind.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

import org.ligo.lab.data.Provided;
import org.ligo.lab.typebinders.CollectionBinder;
import org.ligo.lab.typebinders.Environment;
import org.ligo.lab.typebinders.IteratorBinder;
import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.TypeBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultIteratorBinder<TYPE> extends AbstractBinder<Iterator<TYPE>> implements IteratorBinder<TYPE> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultIteratorBinder.class);
	
	private final Environment env;
	private final CollectionBinder<List<TYPE>,TYPE> backing;
	
	@SuppressWarnings("unchecked")
	DefaultIteratorBinder(final Key<Iterator<TYPE>> key, Environment e) {
		super(key);
		env=e;
		
		//(note this synthetic type will not hit cache)
		final ParameterizedType current = GENERIC(key.kind());
		ParameterizedType pt = new ParameterizedType() {
			@Override public Type getRawType() {return List.class;}
			@Override public Type getOwnerType() {return current.getRawType();}
			@Override public Type[] getActualTypeArguments() {return current.getActualTypeArguments();}
		};
		
		TypeBinder<List<TYPE>> binder = (TypeBinder) env.binderFor(get(pt,key.qualifier()));
		
		
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
		return "iterator("+binder()+")";
	}
	
	public static class IteratorBinderProvider implements BinderProvider<Iterator<Object>> {

			@Override public TypeBinder<Iterator<Object>> binder(Key<Iterator<Object>> key, Environment env) {
				return new DefaultIteratorBinder<Object>(key, env);
			}
			
			@SuppressWarnings("unchecked")
			@Override public Key<Iterator<Object>> matchingKey() {
				return (Key) get(Iterator.class);
			}
			
	};
	
}
