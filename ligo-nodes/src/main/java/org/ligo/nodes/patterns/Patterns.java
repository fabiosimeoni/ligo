/**
 * 
 */
package org.ligo.nodes.patterns;

import static java.util.Arrays.*;
import static org.ligo.nodes.model.impl.Nodes.*;

import javax.xml.namespace.QName;

import org.ligo.nodes.patterns.constraints.AnyValue;
import org.ligo.nodes.patterns.edgepatterns.ManyEdges;
import org.ligo.nodes.patterns.edgepatterns.OneEdge;
import org.ligo.nodes.patterns.edgepatterns.OptionalEdge;
import org.ligo.nodes.patterns.nodepatterns.AnyNode;
import org.ligo.nodes.patterns.nodepatterns.InnerNodePattern;
import org.ligo.nodes.patterns.nodepatterns.IntegerLeaf;
import org.ligo.nodes.patterns.nodepatterns.StringLeaf;

/**
 * @author Fabio Simeoni
 *
 */
public class Patterns {

	public static AnyNode any = AnyNode.INSTANCE;
	
	public static InnerNodePattern node = new InnerNodePattern();
	
	public static InnerNodePattern node(EdgePattern ... ps) {
		return new InnerNodePattern(asList(ps));
	}
	
	
	
	public static EdgePattern cond(EdgePattern p) {
		p.flip();
		return p;
	}
	
	public static OneEdge one(String l,NodePattern p) {
		return new OneEdge(lbl(l),p);
	}
	
	public static OneEdge one(QName l, NodePattern p) {
		return new OneEdge(l,p);
	}
	
	public static OptionalEdge opt(String l,NodePattern p) {
		return new OptionalEdge(lbl(l),p);
	}
	
	public static OptionalEdge opt(QName l, NodePattern p) {
		return new OptionalEdge(l,p);
	}
	
	public static ManyEdges many(String l,NodePattern p) {
		return new ManyEdges(lbl(l),p);
	}
	
	public static ManyEdges many(QName l, NodePattern p) {
		return new ManyEdges(l,p);
	}
	
	public static StringLeaf string = new StringLeaf(AnyValue.INSTANCE);
	public static IntegerLeaf integer = new IntegerLeaf(AnyValue.INSTANCE);
}
