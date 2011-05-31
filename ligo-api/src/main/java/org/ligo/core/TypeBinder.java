/**
 * 
 */
package org.ligo.core;

import java.util.List;

import org.ligo.binders.Binder;
import org.ligo.core.annotations.Bind.Mode;
import org.ligo.core.data.Provided;
import org.ligo.core.keys.Key;



/**
 * 
 * A {@link Binder}s of {@link Provided}s and typed instances.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> <em>the bound type</em>, i.e. the type of the bound instances.
 *
 */
public interface TypeBinder<T> extends Binder<List<Provided>,T> {

	/**
	 * Returns the key of the bound type.
	 * @return the key
	 */
	Key<? extends T> key();
	
	/**{@inheritDoc}*/
	public T bind(Provided provided);
	
	/**
	 * Sets the binding {@link Mode} on the binder.
	 * @param m the mode.
	 */
	void setMode(Mode m);
	
	Mode mode();

}
