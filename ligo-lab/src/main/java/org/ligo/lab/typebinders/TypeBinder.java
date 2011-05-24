/**
 * 
 */
package org.ligo.lab.typebinders;

import java.util.List;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.data.Provided;
import org.ligo.lab.typebinders.Bind.Mode;



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
	Key<TYPE> key();
	
	/**
	 * Sets the binding {@link Mode} on the binder.
	 * @param m the mode.
	 */
	void setMode(Mode m);

}