/**
 * 
 */
package org.ligo.nodes.patterns.edgepatterns;

import static java.lang.String.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.nodes.model.api.Edge;
import org.ligo.nodes.patterns.EdgePattern;
import org.ligo.nodes.patterns.NodePattern;

/**
 * @author Fabio Simeoni
 *
 */
public class OptionalEdge extends EdgePattern {

	/**
	 * 
	 */
	public OptionalEdge(QName l, NodePattern p) {
		super(l,p);
	}
	
	/**{@inheritDoc}*/
	public List<Edge> extract(List<Edge> edges) {
		
		//cardinality check
		if (edges.size()>1) 
			throw new RuntimeException("expected at most one "+label()+", found "+edges.size());
		
		List<Edge> matched = new ArrayList<Edge>();
		
		if (edges.size()==1) {
			Edge edge = edges.get(0);
			try {				
				//dispatch to target predicate
				target().extract(edge.target());
				matched.add(edge);

			} catch(Exception tolerate) {}
		}
		
		return matched;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(super.toString(),"opt");
	}
}
