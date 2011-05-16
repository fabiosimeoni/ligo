/**
 * 
 */
package org.ligo.nodes.model.api;

import org.ligo.api.data.DataProvider;


/**
 * 
 * @author Fabio Simeoni
 *
 */
public interface Node {

	
	Node cloneNode();
	DataProvider provider();
	
}
