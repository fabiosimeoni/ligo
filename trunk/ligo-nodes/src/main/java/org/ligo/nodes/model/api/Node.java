/**
 * 
 */
package org.ligo.nodes.model.api;

import org.ligo.core.data.LigoData;
import org.ligo.core.data.LigoProvider;


/**
 * 
 * @author Fabio Simeoni
 *
 */
public interface Node extends LigoProvider {

	
	Node cloneNode();
	LigoData provide();
	
}
