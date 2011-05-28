/**
 * 
 */
package org.ligo.lab.core.impl;

import static org.ligo.lab.core.Key.*;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;

import org.ligo.lab.core.CollectionBinder;
import org.ligo.lab.core.Environment;
import org.ligo.lab.core.IteratorBinder;
import org.ligo.lab.core.Key;
import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.data.Provided;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni
 *
 */
class DefaultIteratorBinder<TYPE> extends AbstractBinder<Iterator<TYPE>> implements IteratorBinder<TYPE> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultIteratorBinder.class);
	
	private final Environment env;
	private final CollectionBinder<List<TYPE>,TYPE> backing;
	
	DefaultIteratorBinder() {
		this(null,new DefaultEnvironment());
	}
	
	@SuppressWarnings("unchecked")
	DefaultIteratorBinder(Class<? extends Annotation> qualifier, Environment e) {
		
		super((Class)Iterator.class,qualifier);
		env=e;
		
		TypeBinder<List<TYPE>> binder = (TypeBinder) env.binderFor(get(List.class,qualifier));
		

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

		/**{@inheritDoc}*/
		@Override
		public TypeBinder<Iterator<Object>> binder(Class<Iterator<Object>> clazz,Class<? extends Annotation> qualifier, Environment env) {
			return new DefaultIteratorBinder<Object>(qualifier,env);
		}			
		
		@SuppressWarnings("unchecked")
		@Override public Key<Iterator<Object>> matchingKey() {
			return (Key) get(Iterator.class);
		}
			
	};
	
}
