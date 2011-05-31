/**
 * 
 */
package org.ligo.nodes.patterns;

import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.nodes.model.api.Edge;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class EdgePattern {
	
	private NodePattern target;
	private QName label;
	private boolean condition;
	
	/**
	 * 
	 */
	public EdgePattern(QName l, NodePattern p) {
		label=l;
		target=p;
	}
	
	/**
	 * @return the label
	 */
	public QName label() {
		return label;
	}
	
	/**
	 * @return the target
	 */
	public NodePattern target() {
		return target;
	}
	
	/**
	 * @return the condition
	 */
	public boolean isCondition() {
		return condition;
	}
	
	public void flip() {
		condition = !condition;
	}
	
	public abstract List<Edge> extract(List<Edge> edges);
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return (isCondition()?"[COND]":"")+"%1s "+label+":"+target;
		
	}
}
