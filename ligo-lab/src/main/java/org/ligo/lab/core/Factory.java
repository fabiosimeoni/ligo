/**
 * 
 */
package org.ligo.lab.core;

import java.util.List;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.BinderFactory;
import org.ligo.lab.core.data.Provided;
import org.ligo.lab.core.impl.DefaultEnvironment;

/**
 * A factory of {@link TypeBinder}s.
 * 
 * @author Fabio Simeoni
 * 
 * @param <TYPE> the bound type of the binders.
 */
public class Factory<TYPE> implements BinderFactory<TYPE, List<Provided>,TYPE> {

	private final Environment context;
	
	/**
	 * Creates an instance with defaults dependencies.
	 */
	public Factory() {
		this(new DefaultEnvironment());
	}
	
	/**
	 * Creates an instance with for a given binding {@link Environment}.
	 */
	public Factory(Environment ctxt) {
		context = ctxt;
	}
	
	/**{@inheritDoc}*/
	@Override
	public Binder<List<Provided>,TYPE> binderFor(Key<TYPE> key) {
		return context.binderFor(key);
	}
}
