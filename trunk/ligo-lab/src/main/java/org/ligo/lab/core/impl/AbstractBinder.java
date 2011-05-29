/**
 * 
 */
package org.ligo.lab.core.impl;

import static org.ligo.lab.core.annotations.Bind.Mode.*;
import static org.ligo.lab.core.keys.Keys.*;

import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.annotations.Bind.Mode;
import org.ligo.lab.core.keys.Key;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractBinder<TYPE> implements TypeBinder<TYPE> {

	protected static final String KIND_ERROR="unexpected type %1s";
	protected static String BINDING_ERROR = "[%1s] could not bind %2s to %3s";
	protected static String BINDING_SUCCESS_LOG = "[{}] bound {} to {} as {}";
	
	
	private final Key<? extends TYPE> key;
	
	private Mode mode = STRICT;
	
	protected AbstractBinder(Class<? extends TYPE> clazz) {
		this.key=newKey(clazz,null);
	}
	
	protected AbstractBinder(Key<? extends TYPE> key) {
		this.key=key;
	}
	
	
	public Key<? extends TYPE> key() {
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
