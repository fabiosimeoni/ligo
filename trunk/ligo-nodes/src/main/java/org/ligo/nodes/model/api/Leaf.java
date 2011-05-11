/**
 * 
 */
package org.ligo.nodes.model.api;


/**
 * 
 * @author Fabio Simeoni
 *
 */
public interface Leaf extends Node {

	String value();
	
	/**{@inheritDoc}*/
	@Override
	public Leaf cloneNode();

}
