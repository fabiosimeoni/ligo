/**
 * 
 */
package org.ligo.core.utils;


/**
 * @author Fabio Simeoni
 *
 */
public class ReflectionUtils {

	public static <T> T valueOf(Class<T> c, String val) {
		
		Object value;
		
		if (c==Character.class || c==Character.TYPE) {
			if (val.length()==1)
				value= val.charAt(0);
			else throw new IllegalArgumentException();
		}
		else
			if (c==Boolean.class || c==Boolean.TYPE)
					value=Boolean.valueOf(val);
		else
			if (c==Byte.class || c==Byte.TYPE)
				value=Byte.valueOf(val);
		else  
			if (c==Short.class || c==Short.TYPE)
				value=Short.valueOf(val);
		else
			if (c==Integer.class || c==Integer.TYPE)
				value=Integer.valueOf(val);
		else
			if (c==Long.class || c==Long.TYPE)
				value=Long.valueOf(val);
		else
			if (c==Float.class || c==Float.TYPE)
				value=Float.valueOf(val);
		else 
			if (c==Double.class || c==Double.TYPE)
				value=Double.valueOf(val);
		else
			if (c==String.class)
				value=val;
		else
			throw new IllegalArgumentException(c+" is not a known primitive");
		
		@SuppressWarnings("unchecked")
		T typed  = (T) value;
		
		return typed;
	}

	public static Class<?> wrapperOf(Class<?> c) {
		
		if (!c.isPrimitive())
			throw new IllegalArgumentException(c+" is not a primitive");
		
		if (c==Byte.TYPE)
			return Byte.class;
		else  
			if (c==Short.TYPE)
				return Short.class;
		else
			if (c==Integer.TYPE)
				return Integer.class;
		else
			if (c==Long.TYPE)
				return Long.class;
		else
			if (c==Float.TYPE)
				return Float.class;
		else 
			if (c==Double.TYPE)
				return Double.class;
		else 
			if (c==Boolean.TYPE)
				return Boolean.class;
		else			
			return Character.class;	
	}
	
	public static <T> T defaultOf(Class<T> c) {
		
		Object value;
		 if (c==Boolean.TYPE || c==Boolean.class)
		      value= Boolean.FALSE;
		 else 
			 if (c==Character.TYPE || c==Character.class)
				 value= '\u0000';
		 else 
			if (c==Long.TYPE || c==Long.class)
				value= 0L;
		else 
			if (c==Integer.TYPE || c==Integer.class)
				value= 0;	
		else 
			if(c==Short.TYPE || c==Short.class)
				value= (short) 0;
		else 
			if (c==Byte.TYPE || c==Byte.class)
				value= (byte) 0;
		else 
			if(c==Float.TYPE || c==Float.class)
				value= 0f;
		else 
			if(c==Double.TYPE || c==Double.class)
				value= 0d;
		else
			if (c==Byte.TYPE || c==Byte.class)
				value= (byte) 0;
		 else
			 value= null;
		 
		 @SuppressWarnings("unchecked")
		 T output = (T) value;
		 return output;
	}
}
