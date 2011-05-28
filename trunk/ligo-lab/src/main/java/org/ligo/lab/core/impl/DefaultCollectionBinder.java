/**
 * 
 */
package org.ligo.lab.core.impl;

import static java.util.Collections.*;
import static org.ligo.lab.core.annotations.Bind.Mode.*;
import static org.ligo.lab.core.keys.Keys.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ligo.lab.core.CollectionBinder;
import org.ligo.lab.core.Environment;
import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.data.Provided;
import org.ligo.lab.core.keys.ClassKey;
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
		this(newKey(clazz),new DefaultEnvironment());
	}
	
	DefaultCollectionBinder(ClassKey<? extends COLLTYPE> key, Environment e) {
		super(key);
		env=e;
		
		setMode(LAX);
		
		Class<?> clazz = key.kind().toClass();
		@SuppressWarnings("unchecked")
		TypeBinder<TYPE> objectBinder = (TypeBinder) env.binderFor(newKey(clazz.getTypeParameters()[0]));
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
		public TypeBinder<COLLTYPE> binder(ClassKey<COLLTYPE> key, Environment env) {
			return new DefaultCollectionBinder<COLLTYPE,TYPE>(key, env);
		}
		
		@Override public ClassKey<COLLTYPE> matchingKey() {
			return newKey(clazz);
		}
			
		};
	}
	
}
