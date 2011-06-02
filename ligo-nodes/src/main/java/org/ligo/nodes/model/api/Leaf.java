/**
 * 
 */
package org.ligo.nodes.model.api;

import org.ligo.data.LigoValue;


/**
 * 
 * @author Fabio Simeoni
 *
 */
public interface Leaf extends Node,LigoValue {

	String value();
	
	/**{@inheritDoc}*/
	@Override
	public Leaf cloneNode();

}
