/**
 * 
 */
package org.ligo.lab.typebinders;

import java.util.Map;

import javax.xml.namespace.QName;

/**
 * A {@link TypeBinder} for generic object types.
 * 
 * @author Fabio Simeoni
 *
 */
public interface ObjectBinder<TYPE> extends TypeBinder<TYPE> {

	Map<QName,TypeBinder<?>> parts();
}
