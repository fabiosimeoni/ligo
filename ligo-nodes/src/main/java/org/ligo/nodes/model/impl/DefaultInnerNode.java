/**
 * 
 */
package org.ligo.nodes.model.impl;

import static java.lang.System.*;
import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.ligo.core.data.LigoData;
import org.ligo.core.data.LigoProvider;
import org.ligo.core.data.LigoObject;
import org.ligo.nodes.model.api.Edge;
import org.ligo.nodes.model.api.InnerNode;
import org.ligo.nodes.model.api.Node;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultInnerNode implements InnerNode {

	final List<Edge> edges = new LinkedList<Edge>();
	final Map<QName,List<Edge>> edgeMap = new HashMap<QName,List<Edge>>();
	
	
	/**
	 * 
	 */
	public DefaultInnerNode(List<Edge> edges) {
		if (edges==null)
			throw new IllegalArgumentException("null input");
		
		for (Edge e : edges) {
			
			this.edges.add(e);
			
			List<Edge> sameLabel = edgeMap.get(e.label());
			if (sameLabel==null) {
				sameLabel = new LinkedList<Edge>();
				edgeMap.put(e.label(),sameLabel);
			}
			sameLabel.add(e);	
		}
	} 
	
	/**{@inheritDoc}*/
	@Override
	public synchronized List<QName> labels() {
		List<QName> labels  = new LinkedList<QName>();
		for (Edge e : edges)
			labels.add(e.label());
		return labels;
	}
	
	
	/**{@inheritDoc}*/
	@Override
	public synchronized List<Edge> edges() {
		return unmodifiableList(edges);
	}

	/**{@inheritDoc}*/
	@Override
	public synchronized List<Edge> edges(QName label) {
		List<Edge> sameLabel = edgeMap.get(label);
		return sameLabel==null?new LinkedList<Edge>():unmodifiableList(sameLabel);
	}
	
	/**{@inheritDoc}*/
	@Override
	public synchronized List<Edge> edges(String label) {
		return edges(new QName(label));
	}

	/**{@inheritDoc}*/
	@Override
	public synchronized Edge edge(QName label) throws IllegalStateException {
		List<Edge> sameLabel = edgeMap.get(label);
		if (sameLabel==null || sameLabel.size()!=1)
			throw new IllegalStateException("zero or many edges with label "+label);
		return sameLabel.get(0);
	}
	
	/**{@inheritDoc}*/
	@Override
	public synchronized Edge edge(String label) throws IllegalStateException {
		return edge(new QName(label));
	}
	
	/**{@inheritDoc}*/
	@Override
	public synchronized boolean hasEdge(QName label) {
		return edgeMap.containsKey(label);
	}
	
	/**{@inheritDoc}*/
	@Override
	public synchronized boolean hasEdge(String label) {
		return hasEdge(new QName(label));
	}
	
	
	/**{@inheritDoc}*/
	@Override
	public synchronized List<Node> children() {
		List<Node> children = new LinkedList<Node>();
		for (Edge e : edges) 
			children.add(e.target());
		return children;
	}

	/**{@inheritDoc}*/
	@Override
	public synchronized List<Node> children(QName label) {
		List<Node> children = new LinkedList<Node>();
		for (Edge e : edges(label))
			children.add(e.target());
		return children;
	}
	/**{@inheritDoc}*/
	@Override
	public synchronized List<Node> children(String label) {
		return children(new QName(label));
	}

	
	/**{@inheritDoc}*/
	@Override
	public synchronized <T extends Node> List<T> children(Class<T> type) {
		List<T> children = new LinkedList<T>();
		for (Node child : children())
			if (type.isInstance(child))
				children.add(type.cast(child));
		return children;
	}

	/**{@inheritDoc}*/
	@Override
	public synchronized <T extends Node> List<T> children(Class<T> type, QName label) {
		List<T> children = new LinkedList<T>();
		for (Node child : children(label))
			if (type.isInstance(child))
				children.add(type.cast(child));
		return children;
	}
	
	/**{@inheritDoc}*/
	@Override
	public synchronized <T extends Node> List<T> children(Class<T> type,String label) {
		return children(type,new QName(label));
	}
	
	/**{@inheritDoc}*/
	@Override
	public synchronized Node child(QName label) throws IllegalStateException {
		return edge(label).target();
	}
	
	/**{@inheritDoc}*/
	@Override
	public synchronized Node child(String label) throws IllegalStateException {
		return child(new QName(label));
	}

	/**{@inheritDoc}*/
	@Override
	public synchronized boolean hasChild(QName label) {
		return !children(label).isEmpty();
	}
	
	/**{@inheritDoc}*/
	@Override
	public synchronized boolean hasChild(String label) {
		return hasChild(new QName(label));
	}
	
	/**{@inheritDoc}*/
	@Override
	public synchronized <T extends Node> T child(Class<T> type, QName label) throws IllegalStateException {
		Node child = child(label);
		if (type.isInstance(child))
			return type.cast(child);
		else
			throw new IllegalStateException("zero or more children of type "+type.getSimpleName());
	}
	
	/**{@inheritDoc}*/
	@Override
	public synchronized <T extends Node> T child(Class<T> type, String label) throws IllegalStateException {
		return child(type,new QName(label));
	}



	//recursive helper
	synchronized List<Node> descendantsRec(QName ... labels) throws IllegalArgumentException {
		
		if (labels.length==1) 
			return children(labels[0]);
		
		List<Node> nodes = new LinkedList<Node>();
		
		QName label = labels[0];
		QName[] rest = new QName[labels.length-1];
		arraycopy(labels, 1, rest, 0, labels.length-1);
		
		for (Node child : children(label))
			if (child instanceof InnerNode)	
				nodes.addAll(((DefaultInnerNode)child).descendantsRec(rest));
		
		return nodes;
	}
	
	/**{@inheritDoc}*/
	@Override
	public synchronized List<Node> descendants(QName... labels) throws IllegalArgumentException {
		if (labels==null || labels.length==0) 
			throw new IllegalArgumentException("no labels");
		return descendantsRec(labels);
	}
	
	/**{@inheritDoc}*/
	@Override
	public synchronized List<Node> descendants(String... labels) throws IllegalArgumentException {
		if (labels==null || labels.length==0) 
			throw new IllegalArgumentException("no labels");
		List<QName> qnames = new LinkedList<QName>();
		for (String l : labels)
			qnames.add(new QName(l));
		return descendantsRec(qnames.toArray(new QName[0]));
	}

	/**{@inheritDoc}*/
	@Override
	public <T extends Node> List<T> descendants(Class<T> type, QName... labels) {
		List<T> typed = new LinkedList<T>();
		for (Node n : descendants(labels))
			if (type.isInstance(n))
				typed.add(type.cast(n));
		return typed;
	}
	
	/**{@inheritDoc}*/
	@Override
	public <T extends Node> List<T> descendants(Class<T> type, String... labels) {
		List<QName> qnames = new LinkedList<QName>();
		for (String l : labels)
			qnames.add(new QName(l));
		return descendants(type,qnames.toArray(new QName[0]));
	}


	/**{@inheritDoc}*/
	@Override
	public synchronized void remove(List<Edge> edges) {
		for (Edge e : edges)
			if (this.edges.remove(e))
				edgeMap.remove(e.label());
	}

	/**{@inheritDoc}*/
	@Override
	public synchronized InnerNode cloneNode() {
		List<Edge> clones = new LinkedList<Edge>();
		for (Edge e : edges) 
			clones.add(e.cloneEdge());
		return new DefaultInnerNode(clones);
	}

	/**{@inheritDoc}*/
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("[");
		for (Edge e : edges) b.append(e+" ");
		b.append("]");
		return b.toString();
	}
	
	/**{@inheritDoc}*/
	@Override
	public LigoData provide() {
		return new LigoObject() {
			public List<LigoProvider> get(QName name) {
				List<LigoProvider> provided = new ArrayList<LigoProvider>();
				for (Node n: children(name))
					provided.add(n);
				return provided;
			}
			public String toString() {
				return DefaultInnerNode.this.toString();
			}
		};
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edges == null) ? 0 : edges.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DefaultInnerNode))
			return false;
		DefaultInnerNode other = (DefaultInnerNode) obj;		
		if (edges == null) {
			if (other.edges != null)
				return false;
		} else if (!edges.equals(other.edges))
			return false;
		return true;
	}

}
