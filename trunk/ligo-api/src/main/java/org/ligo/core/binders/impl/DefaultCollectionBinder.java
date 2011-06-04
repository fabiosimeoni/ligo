/**
 * 
 */
package org.ligo.core.binders.impl;

import static java.lang.String.*;
import static java.util.Collections.*;
import static org.ligo.core.binders.api.BindMode.*;
import static org.ligo.core.keys.Keys.*;
import static org.ligo.core.utils.Constants.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.ligo.core.binders.api.CollectionBinder;
import org.ligo.core.binders.api.Environment;
import org.ligo.core.binders.api.TypeBinder;
import org.ligo.core.keys.Key;
import org.ligo.data.LigoData;
import org.ligo.data.LigoObject;
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
	public COLLTYPE bind(List<? extends LigoData> data) {
		
		List<TYPE> temp = new ArrayList<TYPE>();
		
		//infer collection node
		if (data.size()==1) {
			LigoData datum = data.get(0);
			if (datum instanceof LigoObject) {
				LigoObject object = (LigoObject) data.get(0);
				Set<QName> names = object.names();
				if (names.size()==1 && names.iterator().next().equals(NONAME_QNAME)) {
					logger.trace("processing collection node {}",datum);
					return bind(object.data(NONAME_QNAME));
				}
			}
		}
		
		loop:for (LigoData datum : data)
			try {
				temp.add(binder.bind(singletonList(datum)));
			}
			catch(RuntimeException e) {
				switch(mode()) {
					case STRICT: throw e; 
					case LAX: 
						logger.warn("skipping "+datum+", which cannot be bound",e);
						continue loop;
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
