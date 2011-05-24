/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.ligo.lab.typebinders.Key.*;
import static org.ligo.lab.typebinders.kinds.Kind.*;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ligo.lab.data.Provided;
import org.ligo.lab.typebinders.CollectionBinder;
import org.ligo.lab.typebinders.Environment;
import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.TypeBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultCollectionBinder<COLLTYPE extends Collection<TYPE>,TYPE> extends AbstractBinder<COLLTYPE> implements CollectionBinder<COLLTYPE,TYPE> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultCollectionBinder.class);
	
	private final Environment env;
	
	private TypeBinder<?> binder;
	
	DefaultCollectionBinder(Key<COLLTYPE> key, Environment e) {
		super(key);
		env=e;
		
		switch (key.kind().value()) {
			case CLASS: //simply type-extract class itself
				break;
			case GENERIC: //extract raw type and store variable bindings in the environment
				ParameterizedType pt = GENERIC(key.kind());
				binder = env.binderFor(get(pt.getActualTypeArguments()[0]));
				break;
			default:
				throw new RuntimeException("unexpected kind "+key.kind());
		}
		
	}
	
	@Override
	public TypeBinder<?> binder() {
		return binder;
	}
	@Override
	public COLLTYPE bind(List<Provided> provided) {
		
		List<Object> temp = new ArrayList<Object>();
		
		for (Provided p : provided)
			temp.add(binder.bind(singletonList(p)));
		
		COLLTYPE list = env.resolver().resolve(key(),asList(new Object[]{temp}));
		
		return list;
	}

	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return "coll("+binder+")";
	}
	
	public static <COLLTYPE extends Collection<TYPE>, TYPE> BinderProvider<COLLTYPE> provider(final Class<COLLTYPE> clazz) {
		return new BinderProvider<COLLTYPE>() {

			@Override public TypeBinder<COLLTYPE> binder(Key<COLLTYPE> key, Environment env) {
				return new DefaultCollectionBinder<COLLTYPE,TYPE>(key, env);
			}
			
			@Override public Key<COLLTYPE> matchingKey() {
				return get(clazz);
			}
			
		};
	}
	
}
