/**
 * 
 */
package org.ligo.lab.core.impl;

import static java.lang.String.*;
import static org.ligo.lab.core.kinds.Kind.*;
import static org.ligo.lab.core.utils.ReflectionUtils.*;

import java.lang.reflect.Type;
import java.util.List;

import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.annotations.Bind.Mode;
import org.ligo.lab.core.data.Provided;
import org.ligo.lab.core.keys.Key;

/**
 * @author Fabio Simeoni
 *
 */
public class ConstantBinder implements TypeBinder<Object>  {

	private Object constant;
	private Class<?> clazz;
	private Mode mode;
	/**
	 * 
	 */
	public ConstantBinder(Type t) {
		
		 setClass(t);
		 constant = defaultOf(clazz);

		 
	}
	
	public ConstantBinder(Object co, Type t) {
		
		setClass(t);
		
		if ((co==null && clazz.isPrimitive()) ||
			(co!=null && !clazz.isAssignableFrom(co.getClass())))
				throw new RuntimeException(format("cannot set constant %1s on %2s",co,clazz));
			
		constant=co;
	}
	
	
	void setClass(Type t) {
		
		clazz = kindOf(t).toClass();
		
		if (clazz==null)
			throw new RuntimeException(format("cannot bind constant to %1s",clazz));
	}
	
	/**{@inheritDoc}*/
	@Override
	public Object bind(List<Provided> i) {
		return constant;
	}
	
	/**{@inheritDoc}*/
	@Override
	public Object bind(Provided provided) {
		return constant;
	}
	
	/**{@inheritDoc}*/
	@Override
	public Key<? extends Object> key() {
		return null;
	}
	
	/**{@inheritDoc}*/
	@Override
	public void setMode(Mode m) {
		mode=m;
	}
	
	/**{@inheritDoc}*/
	@Override
	public Mode mode() {
		return mode;
	}
}
