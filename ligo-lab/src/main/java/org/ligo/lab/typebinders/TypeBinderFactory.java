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
public class TypeBinderFactory<TYPE> implements BinderFactory<TYPE, List<Provided>,TYPE> {

	private final Environment context;
	
	/**
	 * 
	 */
	public TypeBinderFactory(Environment ctxt) {
		context = ctxt;
	}
	
	/**{@inheritDoc}*/
	@Override
	public Binder<List<Provided>,TYPE> bind(Key<TYPE> key) {
		return context.binder(key);
	}
}
