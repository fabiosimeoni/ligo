/**
 * 
 */
package org.ligo.api;


/**
 * @author Fabio Simeoni
 *
 */
public interface Binder<IN> {

	
	<T> T bind(Class<T> c, IN m);
}
