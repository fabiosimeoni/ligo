/**
 * 
 */
package org.ligo.lab.typebinders;

import java.util.Collection;


/**
 * A {@link TypeBinder} for {@link Collection} types.
 * 
 * @author Fabio Simeoni
 *
 */
public interface CollectionBinder<COLLTYPE extends Collection<TYPE>,TYPE> extends TypeBinder<COLLTYPE>{

	/**
	 * Returns a binder for the elements of the collection to be bound.
	 * @return the binder.
	 */
	TypeBinder<?> binder();
}
