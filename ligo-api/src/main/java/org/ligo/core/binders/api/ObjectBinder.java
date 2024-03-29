/**
 * 
 */
package org.ligo.core.binders.api;

import java.util.Map;

import javax.xml.namespace.QName;

/**
 * A {@link TypeBinder} for an arbitrary bound type.
 * <P>
 * Analyses the bound type to identify binding requirements for its instances, i.e. named data properties 
 * to be provided by the input {@link LigoProvider}s .
 * 
 * @author Fabio Simeoni
 * 
 * @param <TYPE> the bound type.
 *
 */
public interface ObjectBinder<TYPE> extends TypeBinder<TYPE> {

	/**
	 * Returns binders for individual data properties to be bound.
	 * @return the binders.
	 */
	Map<QName,TypeBinder<?>> binders();
}
