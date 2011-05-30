/**
 * 
 */
package org.ligo.lab.core.impl;

import static java.lang.String.*;
import static java.util.Collections.*;
import static org.ligo.lab.core.annotations.Bind.Mode.*;
import static org.ligo.lab.core.keys.Keys.*;

import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.annotations.Bind.Mode;
import org.ligo.lab.core.data.Provided;
import org.ligo.lab.core.keys.Key;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractBinder<TYPE> implements TypeBinder<TYPE> {

	static final String UNEXPECTED_TYPE_ERROR= "unexpected type %1s";
	static String BINDING_ERROR = "could not bind %1s to %2s";
	static String BINDING_SUCCESS_LOG = "bound {} to {} as {}";
	
	
	private final Key<? extends TYPE> key;
	
	private Class<TYPE> boundClass;
	
	private Mode mode = STRICT;
	
	protected AbstractBinder(Class<? extends TYPE> clazz) {
		this.key=newKey(clazz,null);
	}
	
	@SuppressWarnings("unchecked")
	protected AbstractBinder(Key<? extends TYPE> key) {
		this.key=key;
		
		boundClass = (Class) key.kind().toClass();
		
		if (boundClass==null)
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
	
	public Class<TYPE> boundClass() {
		return boundClass;
	}
	
	/**{@inheritDoc}*/
	@Override
	public void setMode(Mode m) {
		mode=m;
	}
	
	public Mode mode() {
		return mode;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return mode().toString().substring(0,1).toLowerCase();
	}
}
