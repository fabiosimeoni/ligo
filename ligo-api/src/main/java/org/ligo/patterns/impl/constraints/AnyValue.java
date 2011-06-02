/**
 * 
 */
package org.ligo.patterns.impl.constraints;

import org.ligo.patterns.api.Constraint;


/**
 * @author Fabio Simeoni
 *
 */
public class AnyValue implements Constraint<Object> {

	public static AnyValue INSTANCE = new AnyValue();
	
	private AnyValue(){}
	
	/**{@inheritDoc}*/
	@Override
	public boolean accepts(Object t) {
		return true;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return "any";
	}
}
