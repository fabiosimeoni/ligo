/**
 * 
 */
package org.ligo.nodes.model.api;

import org.ligo.core.data.DataProvider;
import org.ligo.core.data.Provided;


/**
 * 
 * @author Fabio Simeoni
 *
 */
public interface Node extends Provided {

	
	Node cloneNode();
	DataProvider provider();
	
}
