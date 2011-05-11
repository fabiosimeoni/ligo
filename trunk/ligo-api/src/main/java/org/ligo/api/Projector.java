/**
 * 
 */
package org.ligo.api;


/**
 * @author Fabio Simeoni
 *
 */
public interface Projector extends TypedProjector<Object,Object> {

	<T> T project(Class<T> c, Object m);
	
	<T,M> T project(Class<T> c, M m, Pattern<M,? extends Object> p);
	
}
