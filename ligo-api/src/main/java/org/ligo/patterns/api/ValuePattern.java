/**
 * 
 */
package org.ligo.patterns.api;

import org.ligo.data.LigoData;

/**
 * @author Fabio Simeoni
 *
 * @param <T>
 * @param <C>
 */
public interface ValuePattern<T, C extends Constraint<? super T>> {

	/**{@inheritDoc}*/
	LigoData bind(LigoData data);

}