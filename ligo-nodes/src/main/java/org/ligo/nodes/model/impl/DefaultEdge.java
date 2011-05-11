/**
 * 
 */
package org.ligo.nodes.model.impl;

import javax.xml.namespace.QName;

import org.ligo.nodes.model.api.Edge;
import org.ligo.nodes.model.api.Node;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultEdge implements Edge {

	final private QName label;
	final private Node target;
	
	/**
	 * 
	 */
	public DefaultEdge(QName l, Node t) {
		label=l;
		target=t;
	}
	
	/**{@inheritDoc}*/
	@Override
	public QName label() {
		return label;
	}

	/**{@inheritDoc}*/
	@Override
	public Node target() {
		return target;
	}
	
	/**{@inheritDoc}*/
	@Override
	public Edge cloneEdge() {
		return new DefaultEdge(label(),target.cloneNode());
	}

	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return label+":"+target;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DefaultEdge))
			return false;
		DefaultEdge other = (DefaultEdge) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	
}
