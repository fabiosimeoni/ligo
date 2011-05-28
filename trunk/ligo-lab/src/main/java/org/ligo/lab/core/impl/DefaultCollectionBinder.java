/**
 * 
 */
package org.ligo.lab.core.impl;

import static java.util.Collections.*;
import static org.ligo.lab.core.Key.*;
import static org.ligo.lab.core.annotations.Bind.Mode.*;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ligo.lab.core.CollectionBinder;
import org.ligo.lab.core.Environment;
import org.ligo.lab.core.Key;
import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.data.Provided;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni
 *
 */
class DefaultCollectionBinder<COLLTYPE extends Collection<TYPE>,TYPE> extends AbstractBinder<COLLTYPE> implements CollectionBinder<COLLTYPE,TYPE> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultCollectionBinder.class);
	
	private final Environment env;
	
	private TypeBinder<TYPE> binder;
	
	DefaultCollectionBinder(Class<? extends COLLTYPE> clazz) {
		this(clazz,null,new DefaultEnvironment());
	}
	
	DefaultCollectionBinder(Class<? extends COLLTYPE> clazz,Class<? extends Annotation> qualifier, Environment e) {
		super(clazz,qualifier);
		env=e;
		
		setMode(LAX);
		
		@SuppressWarnings("unchecked")
		TypeBinder<TYPE> objectBinder = (TypeBinder) env.binderFor(get(clazz.getTypeParameters()[0]));
		binder = objectBinder;
		
		logger.trace(BUILT_LOG,new Object[]{this,clazz,mode()});
			
	}
	
	@Override
	public TypeBinder<?> binder() {
		return binder;
	}
	
	@Override
	public COLLTYPE bind(List<Provided> provided) {
		
		List<TYPE> temp = new ArrayList<TYPE>();
		
		for (Provided p : provided)
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
		
		COLLTYPE list = env.resolver().resolve(key(),emptyList());
		
		list.addAll(temp);
		
		logger.trace("bound {} to {}",list,provided);
		
		return list;
	}

	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return "coll("+binder+")";
	}
	
	public static <COLLTYPE extends Collection<TYPE>, TYPE> BinderProvider<COLLTYPE> provider(final Class<COLLTYPE> clazz) {
		
		return new BinderProvider<COLLTYPE>() {

		/**{@inheritDoc}*/
		@Override
		public TypeBinder<COLLTYPE> binder(Class<COLLTYPE> clazz,Class<? extends Annotation> qualifier, Environment env) {
			return new DefaultCollectionBinder<COLLTYPE,TYPE>(clazz, qualifier, env);
		}
		
		@Override public Key<COLLTYPE> matchingKey() {
			return get(clazz);
		}
			
		};
	}
	
}
