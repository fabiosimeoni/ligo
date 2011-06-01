/**
 * 
 */
package org.ligo.core;

import java.util.List;

import org.ligo.binders.Binder;
import org.ligo.core.data.LigoProvider;
import org.ligo.core.keys.Key;



/**
 * 
 * A {@link Binder}s of {@link LigoProvider}s and typed instances.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> <em>the bound type</em>, i.e. the type of the bound instances.
 *
 */
public interface TypeBinder<T> extends Binder<LigoProvider,T> {

	/**
	 * Returns the key of the bound type.
	 * @return the key
	 */
	Key<? extends T> key();
	
	/**{@inheritDoc}*/
	public T bind(LigoProvider provided);
	
	/**{@inheritDoc}*/
	public T bind(List<LigoProvider> provided);
	
	/**
	 * Sets the binding {@link BindMode} on the binder.
	 * @param m the mode.
	 */
	void setMode(BindMode m);
	
	BindMode mode();

}
