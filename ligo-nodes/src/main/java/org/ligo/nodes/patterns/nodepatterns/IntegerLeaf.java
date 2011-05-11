/**
 * 
 */
package org.ligo.nodes.patterns.nodepatterns;

import static java.lang.String.*;

import javax.xml.bind.annotation.XmlRootElement;

import org.ligo.nodes.patterns.LeafPattern;
import org.ligo.nodes.patterns.constraints.Constraint;

/**
 * Asserts that a leaf has a text value that satisfies a given constraint.
 * @author Fabio Simeoni (University of Strathclyde)
 *
 */
@XmlRootElement 
public class IntegerLeaf extends LeafPattern<Integer, Constraint<? super Integer>> {
	
	/**
	 * Creates an instance with a given constraint.
	 * @param c the constraint.
	 */
	public IntegerLeaf(Constraint<? super Integer> c) {
		super(c);
	}
	
	/**{@inheritDoc}*/
	@Override protected Integer valueOf(String s) {
		return Integer.valueOf(s);
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(super.toString(),"int");
	}
}