/**
 * 
 */
package org.ligo.nodes.patterns.nodepatterns;

import org.ligo.nodes.model.api.Node;
import org.ligo.nodes.patterns.NodePattern;

/**
 * @author Fabio Simeoni
 *
 */
public class AnyNode extends NodePattern {

	public static AnyNode INSTANCE = new AnyNode();
	
	private AnyNode(){}
	
	/**{@inheritDoc}*/
	@Override
	public Node bind(Node n) {
		return n;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return "any";
	}
}
