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
public class ManyEdges extends EdgePattern {

	/**
	 * 
	 */
	public ManyEdges(QName l, NodePattern p) {
		super(l,p);
	}
	
	/**{@inheritDoc}*/
	public List<Edge> extract(List<Edge> edges) {
		
		List<Edge> matches = new ArrayList<Edge>();
		
		for (Edge e : edges)
			try {
				//dispatch to target predicate
				target().extract(e.target());
				matches.add(e);
				
			} catch(Exception tolerate){}
			
		return matches;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(super.toString(),"many");
	}
}
