/**
 * 
 */
package org.ligo.lab.types.api;

import java.util.Map;

import javax.xml.namespace.QName;


/**
 * @author Fabio Simeoni
 *
 */
public interface ObjectTypeDef<TYPE> extends TypeDef<TYPE> {
	
	Map<QName,TypeDef<?>> attributes();
}
