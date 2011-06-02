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
public class StringPattern extends ValuePattern<String, Constraint<? super String>> {
	
	/**
	 * Creates an instance with a given constraint.
	 * @param c the constraint.
	 */
	public StringPattern(Constraint<? super String> c) {
		super(c);
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(super.toString(),"str");
	}
}