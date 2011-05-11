/**
 * 
 */
package org.ligo.api;


/**
 * @author Fabio Simeoni
 *
 */
public interface Binder<MODEL> {

	
	<T> T bind(Class<T> c, MODEL m);
}
