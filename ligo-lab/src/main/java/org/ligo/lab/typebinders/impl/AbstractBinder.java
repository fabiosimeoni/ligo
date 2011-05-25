/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static org.ligo.lab.typebinders.Bind.Mode.*;

import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.TypeBinder;
import org.ligo.lab.typebinders.Bind.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractBinder<TYPE> implements TypeBinder<TYPE> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractBinder.class);
	
	private final Key<TYPE> key;
	
	private Mode mode = STRICT;
	
	protected AbstractBinder(Key<TYPE> key) {
		logger.trace("building binder for {}",key);
		this.key=key;
	}
	
	
	public Key<TYPE> key() {
		return key;
	}
	
	/**{@inheritDoc}*/
	@Override
	public void setMode(Mode m) {
		mode=m;
	}
	
	Mode mode() {
		return mode;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return key.kind().toString();
	}
}
