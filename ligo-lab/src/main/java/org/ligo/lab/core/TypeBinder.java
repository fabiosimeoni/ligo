/**
 * 
 */
package org.ligo.lab.core;

import java.util.List;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.core.annotations.Bind.Mode;
import org.ligo.lab.core.data.Provided;



/**
 * 
 * A {@link Binder}s of {@link Provided}s and typed instances.
 * 
 * @author Fabio Simeoni
 * 
 * @param <TYPE> <em>the bound type</em>, i.e. the type of the bound instances.
 *
 */
public interface TypeBinder<TYPE> extends Binder<List<Provided>,TYPE> {

	/**
	 * Returns the key of the bound type.
	 * @return the key
	 */
	Key<? extends TYPE> key();
	
	/**
	 * Sets the binding {@link Mode} on the binder.
	 * @param m the mode.
	 */
	void setMode(Mode m);

}
