/**
 * 
 */
package org.ligo.core.binders;

import java.util.List;

/**
 * 
 * @author Fabio Simeoni
 * 
 * @param <TYPE> the bound type.
 *
 */
public interface UnionBinder<T> extends TypeBinder<T> {

	List<TypeBinder<T>> binders();
}
