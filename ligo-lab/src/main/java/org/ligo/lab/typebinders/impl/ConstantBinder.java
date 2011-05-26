/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static java.lang.String.*;
import static org.ligo.lab.typebinders.kinds.Kind.*;

import java.lang.reflect.Type;
import java.util.List;

import org.ligo.lab.data.Provided;
import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.TypeBinder;
import org.ligo.lab.typebinders.Bind.Mode;

/**
 * @author Fabio Simeoni
 *
 */
public class ConstantBinder implements TypeBinder<Object>  {

	private Object constant;
	private Class<?> clazz;
	/**
	 * 
	 */
	public ConstantBinder(Type t) {
		
		 setClass(t);

		 if (clazz==Boolean.TYPE) {
		      constant = Boolean.FALSE;
		 }
		 else if (clazz==Character.TYPE) {
		      constant = '\u0000';
		 }
		 else if (clazz==Long.TYPE || 
				 clazz==Integer.TYPE || 
				 clazz==Short.TYPE || 
				 clazz==Byte.TYPE ||
				 clazz==Float.TYPE || 
				 clazz==Double.TYPE ||
				 clazz==Character.TYPE) {
			 constant = (byte) 0;
		 }
		 else
			 constant = null;
		 
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
	public Key<? extends Object> key() {
		return null;
	}
	
	/**{@inheritDoc}*/
	@Override
	public void setMode(Mode m) {
		// TODO Auto-generated method stub
		
	}
}
