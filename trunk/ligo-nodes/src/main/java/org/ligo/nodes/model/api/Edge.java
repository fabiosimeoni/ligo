/**
 * 
 */
package org.ligo.nodes.model.api;

import javax.xml.namespace.QName;


/**
 * 
 * @author Fabio Simeoni
 *
 */
public interface Edge {

	QName label();
	Node target();
	Edge cloneEdge();
}
