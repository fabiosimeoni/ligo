/**
 * 
 */
package org.ligo.core;

import java.util.List;

import org.ligo.binders.Binder;
import org.ligo.core.data.LigoData;
import org.ligo.core.keys.Key;



/**
 * 
 * A {@link Binder}s of {@link LigoData}s and typed instances.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> <em>the bound type</em>, i.e. the type of the bound instances
 *
 */
public interface TypeBinder<T> extends Binder<LigoData,T> {

	/**
	 * Returns the key of the bound type.
	 * @return the key
	 */
	Key<? extends T> key();
	
	/**{@inheritDoc}*/
	public T bind(LigoData data);
	
	/**{@inheritDoc}*/
	public T bind(List<LigoData> data);
	
	/**
	 * Sets the {@link BindMode} on the binder.
	 * @param m the mode
	 */
	void setMode(BindMode m);
	
	/**
	 * Returns the {@link BindMode} of the binder.
	 * @return the mode
	 */
	BindMode mode();

}