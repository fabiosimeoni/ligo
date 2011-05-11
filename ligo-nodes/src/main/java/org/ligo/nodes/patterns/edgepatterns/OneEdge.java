/**
 * 
 */
package org.ligo.nodes.patterns.edgepatterns;

import static java.lang.String.*;
import static java.util.Collections.*;

import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.nodes.model.api.Edge;
import org.ligo.nodes.patterns.EdgePattern;
import org.ligo.nodes.patterns.NodePattern;

/**
 * @author Fabio Simeoni
 *
 */
public class OneEdge extends EdgePattern {

	/**
	 * 
	 */
	public OneEdge(QName l, NodePattern p) {
		super(l,p);
	}
	
	/**{@inheritDoc}*/
	public List<Edge> extract(List<Edge> edges) {
		
		//cardinality check
		if (edges.size()!=1) 
			throw new RuntimeException("expected one "+label()+" found "+edges.size());
		
		Edge match = edges.get(0);
		
		//dispatch to target predicate
		target().extract(match.target());
		
		return singletonList(match);
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(super.toString(),"one");
	}
}
