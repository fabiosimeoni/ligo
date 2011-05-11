/**
 * 
 */
package org.ligo.nodes;

import static java.lang.String.*;

import org.ligo.api.DefaultProjector;
import org.ligo.api.DefaultTypedProjector;
import org.ligo.api.Projector;
import org.ligo.api.TypedProjector;
import org.ligo.nodes.factory.NodeFactory;
import org.ligo.nodes.factory.XMLNodeFactory;
import org.ligo.nodes.model.api.Node;

/**
 * @author Fabio Simeoni
 *
 */
public class LigoConfiguration {

	private Projector projector;
	private NodeFactory factory;
	
	/**
	 * 
	 */
	public LigoConfiguration() {
		this(new DefaultProjector<Node,Node>(new LigoContext()),new XMLNodeFactory());
	}
	
	public LigoConfiguration(Projector p,  NodeFactory f) {
		projector=p;
		factory=f;
	}
	
	public Projector projector() {
		return projector;
	}
	
	public TypedProjector<Node,Node> typedProjector() {
		return new DefaultTypedProjector<Node,Node>(projector);
	}
	
	public NodeFactory factory() {
		return factory;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format("projector = %1s, model-factory = %3s",projector,factory);
	}
}
