/**
 * 
 */
package org.ligo.lab.typebinders;

import java.util.List;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.BinderFactory;
import org.ligo.lab.data.Provided;

/**
 * @author Fabio Simeoni
 *
 */
public class Factory<TYPE> implements BinderFactory<TYPE, List<Provided>,TYPE> {

	private final Environment context;
	
	/**
	 * 
	 */
	public Factory(Environment ctxt) {
		context = ctxt;
	}
	
	/**{@inheritDoc}*/
	@Override
	public Binder<List<Provided>,TYPE> bind(Key<TYPE> key) {
		return context.bind(key);
	}
}
