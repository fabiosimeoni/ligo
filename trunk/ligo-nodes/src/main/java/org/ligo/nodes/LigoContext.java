/**
 * 
 */
package org.ligo.nodes;

import javax.inject.Inject;

import org.ligo.api.Binder;
import org.ligo.api.PatternFactory;
import org.ligo.api.configuration.AbstractLigoContext;
import org.ligo.nodes.binder.NodeBinder;
import org.ligo.nodes.model.api.Node;
import org.ligo.nodes.patterns.NodePatternFactory;

/**
 * @author Fabio Simeoni
 *
 */
final public class LigoContext extends AbstractLigoContext<Node,Node> {

	public LigoContext() {
		this(new NodeBinder(),new NodePatternFactory());
	}
	
	@Inject
	public LigoContext(Binder<Node> b, PatternFactory<Node,Node> f) {
		super(b,f);
	}
	
	/**{@inheritDoc}*/
	@Override
	public Class<? extends Node> matchType() {
		return Node.class;
	}

	/**{@inheritDoc}*/
	@Override
	public Class<? extends Node> modelType() {
		return Node.class;
	}
	
	

}
