/**
 * 
 */
package org.ligo.core.binders;





/**
 * A {@link TypeBinder} for array types.
 * 
 * @author Fabio Simeoni
 *
 */
public interface ArrayBinder<TYPE> extends TypeBinder<TYPE[]>{

	/**
	 * Returns a binder for the elements of the array to be bound.
	 * @return the binder.
	 */
	TypeBinder<?> binder();
	
}
