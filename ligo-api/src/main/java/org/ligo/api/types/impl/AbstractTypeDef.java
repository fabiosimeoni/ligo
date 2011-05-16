/**
 * 
 */
package org.ligo.api.types.impl;

import org.ligo.api.types.api.TypeDef;
import org.ligo.api.types.api.TypeKey;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractTypeDef<TYPE> implements TypeDef<TYPE> {

	private final TypeKey<TYPE> key;
	
	protected AbstractTypeDef(TypeKey<TYPE> k) {
		key=k;
	}
	
	public TypeKey<TYPE> key() {
		return key;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return key.type().getSimpleName().toString().toLowerCase();
	}
}
