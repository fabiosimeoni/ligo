/**
 * 
 */
package org.ligo.api.types.api;




/**
 * @author Fabio Simeoni
 *
 */
public interface CollectionTypeDef<TYPE> extends TypeDef<TYPE> {
	
	TypeDef<?> member();
}
