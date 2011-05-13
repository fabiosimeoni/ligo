/**
 * 
 */
package org.ligo.api.types.api;


/**
 * @author Fabio Simeoni
 *
 */
public interface TypeDef<TYPE> {

	TypeKey<TYPE> key();
	
	TYPE newInstance(Object data);
}
