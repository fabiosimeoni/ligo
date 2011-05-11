/**
 * 
 */
package org.ligo.nodes;

import org.ligo.api.DefaultTypedProjector;
import org.ligo.api.ProjectionContext;
import org.ligo.nodes.model.api.Node;

/**
 * @author Fabio Simeoni
 *
 */
public class NodeProjector extends DefaultTypedProjector<Node,Node> {

	/**
	 * 
	 */
	public NodeProjector(ProjectionContext<Node,Node> ctxt) {
		super(ctxt);
	}
	
}
