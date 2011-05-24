/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.TypeBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractTypeBinder<TYPE> implements TypeBinder<TYPE> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractTypeBinder.class);
	
	public static enum Mode {STRICT,LAX};
	
	private final Key<TYPE> key;
	
	
	protected AbstractTypeBinder(Key<TYPE> key) {
		logger.trace("processing {}",key);
		this.key=key;
	}
	
	public Key<TYPE> key() {
		return key;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return key.kind().toString();
	}
}
