/**
 * 
 */
package org.ligo.nodes.model.api;

import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.data.LigoObject;

/**
 * 
 * @author Fabio Simeoni
 *
 */
public interface InnerNode extends Node,LigoObject {

	List<QName> labels();

	List<Node> children();
	List<Node> children(QName label);
	List<Node> children(String label);
	<T extends Node> List<T> children(Class<T> type);
	<T extends Node> List<T> children(Class<T> type,QName label);
	<T extends Node> List<T> children(Class<T> type,String label);

	Node child(QName label) throws IllegalStateException;
	Node child(String label) throws IllegalStateException;
	<T extends Node> T child(Class<T> type,QName label) throws IllegalStateException;
	<T extends Node> T child(Class<T> type,String label) throws IllegalStateException;
	boolean hasChild(QName label);
	boolean hasChild(String label);
	
	List<Edge> edges();
	List<Edge> edges(QName label);
	List<Edge> edges(String label);
	
	Edge edge(QName label) throws IllegalStateException;
	Edge edge(String label) throws IllegalStateException;
	boolean hasEdge(QName label);
	boolean hasEdge(String label);
	
	List<Node> descendants(QName ... labels) throws IllegalArgumentException;
	List<Node> descendants(String ... labels) throws IllegalArgumentException;
	<T extends Node> List<T> descendants(Class<T> type,QName ... labels);
	<T extends Node> List<T> descendants(Class<T> type,String ... labels);
	
	void remove(List<Edge> e);
	
	/**{@inheritDoc}*/
	@Override
	public InnerNode cloneNode();
	
	
}
