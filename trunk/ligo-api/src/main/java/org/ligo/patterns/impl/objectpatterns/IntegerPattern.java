/**
 * 
 */
package org.ligo.patterns.impl.objectpatterns;

import static java.lang.String.*;

import javax.xml.bind.annotation.XmlRootElement;

import org.ligo.patterns.api.Constraint;
import org.ligo.patterns.impl.DefaultValuePattern;



/**
 * Asserts that a leaf has a text value that satisfies a given constraint.
 * @author Fabio Simeoni (University of Strathclyde)
 *
 */
@XmlRootElement 
public class IntegerPattern extends DefaultValuePattern<Integer, Constraint<? super Integer>> {
	
	/**
	 * Creates an instance with a given constraint.
	 * @param c the constraint.
	 */
	public IntegerPattern(Constraint<? super Integer> c) {
		super(c);
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(super.toString(),"int");
	}
}