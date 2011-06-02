/**
 * 
 */
package org.ligo.patterns.api;


/**
 * @author Fabio Simeoni
 *
 */
public interface Constraint<T> {	
	
	/**
	 * Indicates whether a value satisfies the constraint.
	 * @param t the value.
	 * @return <code>true</code> if it does, <code>false</code> otherwise.
	 */
	boolean accepts(T t);

}
