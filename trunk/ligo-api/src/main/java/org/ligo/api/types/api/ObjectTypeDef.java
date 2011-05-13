/**
 * 
 */
package org.ligo.api.types.api;

import java.util.Map;


/**
 * @author Fabio Simeoni
 *
 */
public interface ObjectTypeDef<TYPE> extends TypeDef<TYPE> {
	
	Map<String,TypeDef<?>> attributes();
}
