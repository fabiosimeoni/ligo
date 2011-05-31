/**
 * 
 */
package org.ligo.core.impl;

import static java.lang.String.*;
import static java.util.Collections.*;
import static org.ligo.core.BindMode.*;

import org.ligo.core.BindMode;
import org.ligo.core.TypeBinder;
import org.ligo.core.data.Provided;
import org.ligo.core.keys.Key;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractBinder<TYPE> implements TypeBinder<TYPE> {

	static final String UNEXPECTED_TYPE_ERROR= "unexpected type %1s";
	static String BINDING_ERROR = "could not bind %1s to %2s";
	static String BINDING_SUCCESS_LOG = "bound {} to {} as {}";
	
	
	private final Key<? extends TYPE> key;
	
	private BindMode mode = STRICT;
	
	protected AbstractBinder(Key<? extends TYPE> key) {
		
		this.key=key;
		
		if (key.toClass()==null)
			throw new RuntimeException(format(UNEXPECTED_TYPE_ERROR,key.kind()));
	}
	
	
	/**{@inheritDoc}*/
	@Override
	public TYPE bind(Provided provided) {
		return bind(singletonList(provided));
	}
	public Key<? extends TYPE> key() {
		return key;
	}
	
	/**{@inheritDoc}*/
	@Override
	public void setMode(BindMode m) {
		mode=m;
	}
	
	public BindMode mode() {
		return mode;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return mode().toString().substring(0,1).toLowerCase();
	}
}
