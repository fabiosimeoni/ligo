/**
 * 
 */
package org.ligo.nodes.factory;

import java.io.Reader;

import org.ligo.nodes.model.api.Node;


/**
 * @author Fabio Simeoni
 *
 */
public interface NodeFactory {

	Node generate(Reader r);
}
