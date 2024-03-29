/**
 * 
 */
package org.ligo.data.impl;

import org.ligo.data.LigoValue;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultLigoValue extends AbstractLigoValue implements LigoValue {

	private final Object value;
	
	public DefaultLigoValue(Object v) {
		value =v;
	}
	
	/**{@inheritDoc}*/
	@Override
	public Object get() {
		return value;
	}
}
