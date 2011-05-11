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
public class StringLeaf extends LeafPattern<String, Constraint<? super String>> {
	
	/**
	 * Creates an instance with a given constraint.
	 * @param c the constraint.
	 */
	public StringLeaf(Constraint<? super String> c) {
		super(c);
	}
	
	/**{@inheritDoc}*/
	@Override protected String valueOf(String s) {
		return s;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(super.toString(),"str");
	}
}