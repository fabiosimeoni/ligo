/**
 * 
 */
package org.ligo.core;

import java.util.List;

import org.ligo.binders.Binder;
import org.ligo.binders.BinderFactory;
import org.ligo.core.data.Provided;
import org.ligo.core.impl.DefaultEnvironment;
import org.ligo.core.keys.Key;

/**
 * A env of {@link TypeBinder}s.
 * 
 * @author Fabio Simeoni
 * 
 * @param <TYPE> the bound type of the binders.
 */
public class LigoFactory<TYPE> implements BinderFactory<TYPE, List<Provided>,TYPE> {

	private final Environment env;
	
	/**
	 * Creates an instance with defaults dependencies.
	 */
	public LigoFactory() {
		this(new DefaultEnvironment());
	}
	
	/**
	 * Creates an instance with for a given binding {@link Environment}.
	 */
	public LigoFactory(Environment environment) {
		env = environment;
	}
	
	public Resolver getResolver() {
		return env.resolver();
	}
	
	/**{@inheritDoc}*/
	@Override
	public Binder<List<Provided>,TYPE> binderFor(Key<TYPE> key) {
		return env.binderFor(key);
	}
}
