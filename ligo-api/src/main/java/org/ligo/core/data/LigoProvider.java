/**
 * 
 */
package org.ligo.core.data;

/**
 * Expose a {@link LigoData} interface on demand.
 * 
 * @author Fabio Simeoni
 *
 */
public interface LigoProvider {

	LigoData provide();
}
