/**
 * 
 */
package org.ligo.api.types.api;


/**
 * @author Fabio Simeoni
 *
 */
public interface TypeDefFactory {

	<TYPE> TypeDef<TYPE> getTypeDef(Class<TYPE> key);
	<TYPE> TypeDef<TYPE> getTypeDef(TypeKey<TYPE> key);
	
	<TYPE> TYPE getInstance(TypeKey<TYPE> key, Object ... args);
	
}
