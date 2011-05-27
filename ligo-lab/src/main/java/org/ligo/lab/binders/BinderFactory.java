/**
 * 
 */
package org.ligo.lab.binders;

import org.ligo.lab.core.Key;

/**
 * A {@link Key}-driven factory of {@link Binder}s.
 * 
 * @author Fabio Simeoni
 * 
 * @param <TYPE> the type of the key
 * @param <IN> the input type of binders
 * @param <OUT> the output type of binders
 *
 */
public interface BinderFactory<TYPE,IN,OUT> {
	
	/**
	 * Returns a binder for a given key. 
	 * @param key the key.
	 * @return the binder.
	 */
	public Binder<IN, OUT> binderFor(Key<TYPE> key);
}
