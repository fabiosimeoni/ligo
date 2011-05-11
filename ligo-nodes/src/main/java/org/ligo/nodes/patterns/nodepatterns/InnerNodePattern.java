/**
 * 
 */
package org.ligo.nodes.patterns.nodepatterns;

import static java.util.Collections.*;

import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.nodes.model.api.Edge;
import org.ligo.nodes.model.api.InnerNode;
import org.ligo.nodes.model.api.Node;
import org.ligo.nodes.patterns.EdgePattern;
import org.ligo.nodes.patterns.NodePattern;

/**
 * @author Fabio Simeoni
 *
 */
public class InnerNodePattern extends NodePattern {

	List<EdgePattern> patterns;
	
	public InnerNodePattern() {
		patterns = unmodifiableList(new LinkedList<EdgePattern>());
	}
	
	/**
	 * 
	 */
	public InnerNodePattern(List<EdgePattern> ps) {
		patterns=unmodifiableList(new LinkedList<EdgePattern>(ps));
	}
	
	/**
	 * @return the patterns
	 */
	public List<EdgePattern> patterns() {
		return patterns;
	}
	
	/**{@inheritDoc}*/
	@Override
	public Node extract(Node n) {
		
		if (!InnerNode.class.isAssignableFrom(n.getClass()))
			throw new RuntimeException(this+" does not match leaf "+n);
		
		InnerNode node = InnerNode.class.cast(n); 
		
		//start assuming all edges are unmatched
		List<Edge> unmatched = new LinkedList<Edge>(node.edges());
		
		for (EdgePattern ep : patterns) {
			
			//find potential matches
			List<Edge> candidates = new LinkedList<Edge>();
			for (Edge edge : unmatched)
				if (match(ep.label(),edge.label()))
					candidates.add(edge);
			
			List<Edge> matched = ep.extract(candidates);
			
			//discard matches for condition-patterns 
			if (ep.isCondition()) 
				node.remove(matched);
			
			//greedy semantics: remove matches for next predicate
			unmatched.removeAll(matched);

		}
		
		//remove edges that remained unmatched
		node.remove(unmatched);

		return node;
	}
	
	//helper
	private boolean match(QName regexp,QName label) {
		return (label.getNamespaceURI().matches(regexp.getNamespaceURI()) && 
				label.getLocalPart().matches(regexp.getLocalPart()));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((patterns == null) ? 0 : patterns.hashCode());
		return result;
	}
	
	
	/**{@inheritDoc}*/
	@Override public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("[ ");
		for (EdgePattern p : patterns)
			b.append("("+p+") ");
		b.append("]");
		return b.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof InnerNodePattern))
			return false;
		InnerNodePattern other = (InnerNodePattern) obj;
		if (patterns == null) {
			if (other.patterns != null)
				return false;
		} else if (!patterns.equals(other.patterns))
			return false;
		return true;
	}
}
