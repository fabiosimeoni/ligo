/**
 * 
 */
package org.ligo.core;

import java.util.Collection;
import java.util.Iterator;


/**
 * A {@link TypeBinder} for {@link Collection} types.
 * 
 * @author Fabio Simeoni
 *
 */
public interface IteratorBinder<TYPE> extends TypeBinder<Iterator<TYPE>>{

	/**
	 * Returns a binder for the elements of the collection to be bound.
	 * @return the binder.
	 */
	TypeBinder<?> binder();
}
