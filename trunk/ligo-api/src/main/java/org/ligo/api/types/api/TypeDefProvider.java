package org.ligo.api.types.api;

/**
 * 
 * @author Fabio Simeoni
 *
 * @param <TYPE>
 */
public interface TypeDefProvider<TYPE> {
	
	TypeDef<TYPE> generate(TypeKey<TYPE> key);
}