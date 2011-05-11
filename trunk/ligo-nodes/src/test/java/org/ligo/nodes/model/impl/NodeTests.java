/**
 * 
 */
package org.ligo.nodes.model.impl;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.ligo.nodes.model.impl.Nodes.*;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.ligo.nodes.model.api.Edge;
import org.ligo.nodes.model.api.InnerNode;
import org.ligo.nodes.model.api.Leaf;
import org.ligo.nodes.model.api.Node;

/**
 * @author Fabio Simeoni
 *
 */
public class NodeTests {

	@Test
	public void leaves() {
		
		String expected = "hello"; 
		Leaf l = l(expected);
		assertEquals(expected, l.value());
		
		Leaf clone = l.cloneNode();
		
		assertEquals(l,clone);
		
	}
	
	@Test
	public void edges() {
		
		QName lbl = lbl("hello");
		Node target = l("world");
		Edge e = e(lbl,target);
		assertEquals(lbl, e.label());
		assertEquals(target, e.target());
		assertEquals(e, e.cloneEdge());
		
	}
	
	@Test
	public void empty() {
		
		InnerNode node = n();
		assertEquals(emptyList(), node.labels());
		assertEquals(emptyList(), node.edges());
		assertEquals(emptyList(), node.edges("none"));
		assertEquals(emptyList(), node.children());
		assertEquals(emptyList(), node.children("none"));
		assertEquals(emptyList(), node.descendants("none"));
		
		try {
			node.edge("none");
			fail();
		}
		catch(IllegalStateException e) {}
		
		try {
			node.child("none");
			fail();
		}
		catch(IllegalStateException e) {}
		
		try {
			node.child(L,"none");
			fail();
		}
		catch(IllegalStateException e) {}

		
		//inconsequential
		node.remove(singletonList(e("hello","world")));		
		
		assertEquals(n(), node);
		
	}
	
	@Test
	public void parent() {
		
		Edge e1 =e("hello","world");
		Edge e2 = e("ciao","mondo");
		InnerNode node = n(e1,e2);

		
		assertEquals(asList(e1.label(),e2.label()), node.labels());
		assertEquals(asList(e1.target(),e2.target()), node.children());
		assertEquals(asList(e1,e2), node.edges());
		
		assertEquals(e1.target(),node.child("hello"));
		assertEquals(e1,node.edge("hello"));
		assertEquals(asList(e1.target()),node.descendants("hello"));
		
		//order does matter
		assertFalse(node.equals(n(e2,e1)));
		
		Node node3 = n(e("31",0),e("32",1));
		node = n(e1,e2,e("3",node3));
		
		assertEquals(node3, node.child(lbl("3")));
		
		assertEquals("1", node.child(N,"3").child(L,"32").value());
		
		
	}
	
	@Test
	public void deepParent() {
		
		
		Edge e1 =e("hello","world");
		Edge e2 = e("ciao","mondo");
		Node node3 = n(e("31",0),e("32",1));
		InnerNode node = n(e1,e2,e("3",node3));
		
		assertEquals(node3, node.child("3"));
		assertEquals("1", node.child(N,"3").child(L,"32").value());
		assertEquals(asList(l(1)), node.descendants("3","32"));
		assertEquals(emptyList(), node.descendants("31","32"));		
		
	}
	
	@Test
	public void repeatedEdges() {
		
		
		Edge e1 =e("hello","world");
		Edge e2 = e("hello","mondo");
		Node node3 = n(e("31",0),e("32",1));
		InnerNode node = n(e1,e2,e("3",node3));
		
		assertEquals(asList(e1.target(),e2.target()), node.children(e1.label()));
		assertEquals(asList(e1.target(),e2.target()), node.children(e2.label()));
		
		assertEquals(asList(e1,e2), node.edges(e1.label()));
		
		
		try {
			node.child(e1.label());
			fail();
		}
		catch(IllegalStateException e) {}
		
		assertEquals(asList(e1,e2), node.edges(e2.label()));
		try {
			node.edge(e1.label());
			fail();
		}
		catch(IllegalStateException e) {}
		
	}
}
