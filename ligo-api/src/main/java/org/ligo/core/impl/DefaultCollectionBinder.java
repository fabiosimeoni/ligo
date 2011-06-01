/**
 * 
 */
package org.ligo.core.impl;

import static java.lang.String.*;
import static java.util.Collections.*;
import static org.ligo.core.BindMode.*;
import static org.ligo.core.keys.Keys.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ligo.core.CollectionBinder;
import org.ligo.core.Environment;
import org.ligo.core.TypeBinder;
import org.ligo.core.data.LigoProvider;
import org.ligo.core.keys.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni
 *
 */
class DefaultCollectionBinder<COLLTYPE extends Collection<TYPE>,TYPE> extends AbstractBinder<COLLTYPE> implements CollectionBinder<COLLTYPE,TYPE> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultCollectionBinder.class);
	
	static final String TO_STRING= "%1s-coll(%2s)";
	
	private final Environment env;
	
	private TypeBinder<TYPE> binder;
	
	DefaultCollectionBinder(Key<? extends COLLTYPE> key, Environment e) {
		super(key);
		env=e;
		
		setMode(LAX);
	
		@SuppressWarnings("unchecked")
		TypeBinder<TYPE> objectBinder = (TypeBinder) env.binderFor(newKey(key().toClass().getTypeParameters()[0]));
		binder = objectBinder;
	}
	
	@Override
	public TypeBinder<?> binder() {
		return binder;
	}
	
	@Override
	public COLLTYPE bind(List<LigoProvider> provided) {
		
		List<TYPE> temp = new ArrayList<TYPE>();
		
		for (LigoProvider p : provided)
			try {
				temp.add(binder.bind(singletonList(p)));
			}
			catch(RuntimeException e) {
				switch(mode()) {
					case STRICT: throw e; 
					case LAX: 
						logger.warn("skipping "+p+", which cannot be bound",e);
						continue;
				}
				
				
			}
		
		COLLTYPE list = (COLLTYPE) env.resolver().resolve(key().toClass(),emptyList());
		
		list.addAll(temp);
		
		return list;
	}

	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(TO_STRING,super.toString(),binder);
	}
	
	public static <COLLTYPE extends Collection<TYPE>, TYPE> BinderProvider<COLLTYPE> provider(final Class<COLLTYPE> clazz) {
		
		return new BinderProvider<COLLTYPE>() {

		/**{@inheritDoc}*/
		@Override
		public TypeBinder<COLLTYPE> binder(Key<COLLTYPE> key, Environment env) {
			return new DefaultCollectionBinder<COLLTYPE,TYPE>(key, env);
		}
		
		@Override public Key<COLLTYPE> matchingKey() {
			return newKey(clazz);
		}
			
		};
	}
	
}
