/**
 * 
 */
package org.ligo.api.types.impl;

import static java.lang.String.*;

import org.ligo.api.types.api.TypeKey;


/**
 * @author Fabio Simeoni
 *
 */
public class PrimitiveDef<TYPE> extends AbstractTypeDef<TYPE> {
	
	public PrimitiveDef(TypeKey<TYPE> key) {
		super(key);
	}
	
	public TYPE newInstance(Object value) {
		try {
			return key().type().cast(value);
		}
		catch(ClassCastException e) {
			throw new RuntimeException(format("cannot bind %1s to %2s",key().type(),value));
		}
	};
}
