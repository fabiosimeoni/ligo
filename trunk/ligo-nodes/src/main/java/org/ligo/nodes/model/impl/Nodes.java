/**
 * 
 */
package org.ligo.nodes.model.impl;

import static java.util.Arrays.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;

import org.ligo.nodes.model.api.Edge;
import org.ligo.nodes.model.api.InnerNode;
import org.ligo.nodes.model.api.Leaf;
import org.ligo.nodes.model.api.Node;

/**
 * @author Fabio Simeoni
 *
 */
public class Nodes {

	private static DatatypeFactory typeFactory;
	
	static {
		try {
			typeFactory = DatatypeFactory.newInstance();
		}
		catch(DatatypeConfigurationException e) {
			throw new RuntimeException("could not configure datatype factory",e);
		}
	}
	
	/** Constant for the leaf node type. */
	public static final Class<Leaf> L = Leaf.class;
	
	/** Constant for the inner node type. */
	public static final Class<InnerNode> N = InnerNode.class;
	
	

	public static QName lbl(String n) {
		return new QName(n);
	}
	
	public static QName lbl(String ns, String l) {
		return new QName(ns,l);
	}
	
	
	
	
	public static InnerNode n(Edge ...edges) {
		return new DefaultInnerNode(new LinkedList<Edge>(asList(edges)));
	}
	
	
	public static Leaf l(Object v) {
		return new DefaultLeaf(toString(v));
	}
	
	
	
	
	public static Edge e(QName label, Node target) {
		return new DefaultEdge(label, target);
	}
	
	public static Edge e(QName label, Object value) {
		return new DefaultEdge(label, l(value));
	}
	
	public static Edge e(String label, Node target) {
		return new DefaultEdge(lbl(label), target);
	}
	
	public static Edge e(String label, Object value) {
		return new DefaultEdge(lbl(label), l(value));
	}
	
	
	
	
	private static String toString(Object v) {
		return
			v instanceof java.util.Date?toDateString((java.util.Date) v):
				v instanceof Calendar?toDateString(((Calendar)v).getTime()):String.valueOf(v);
	}
	
	public static synchronized String toDateString(java.util.Date date) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(date);
		return typeFactory.newXMLGregorianCalendar(c).toString();
		
	}
}
