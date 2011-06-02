/**
 * 
 */
package org.ligo.patterns.objectpatterns;

import static java.lang.String.*;

import javax.xml.bind.annotation.XmlRootElement;

import org.ligo.patterns.ValuePattern;
import org.ligo.patterns.constraints.Constraint;



/**
 * Asserts that a leaf has a text value that satisfies a given constraint.
 * @author Fabio Simeoni (University of Strathclyde)
 *
 */
@XmlRootElement 
public class IntegerPattern extends ValuePattern<Integer, Constraint<? super Integer>> {
	
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