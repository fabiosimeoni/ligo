/**
 * 
 */
package org.ligo.api;


/**
 * @author Fabio Simeoni
 *
 */
public interface TypedProjector<MODEL,MATCH> {

	<T> T project(Class<T> c, MODEL m);
	
	<T,M extends MODEL> T project(Class<T> c, M m, Pattern<M,? extends MATCH> p);
	
}
