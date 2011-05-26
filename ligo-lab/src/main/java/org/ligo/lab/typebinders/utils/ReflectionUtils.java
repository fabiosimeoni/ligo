/**
 * 
 */
package org.ligo.lab.typebinders.utils;

/**
 * @author Fabio Simeoni
 *
 */
public class ReflectionUtils {

	public static <T> T valueOf(Class<T> c, String val) {
		
		Object value;
		if (c==Character.class) {
			if (val.length()==1)
				value= val.charAt(0);
			else throw new IllegalArgumentException();
		}
		else
			if (c==Byte.class)
				value=Byte.valueOf(val);
		else  
			if (c==Short.class)
				value=Short.valueOf(val);
		else
			if (c==Integer.class)
				value=Integer.valueOf(val);
		else
			if (c==Long.class)
				value=Long.valueOf(val);
		else
			if (c==Float.class)
				value=Float.valueOf(val);
		else 
			if (c==Double.class)
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
	
	public static Object defaultOf(Class<?> c) {
		
		 if (c==Boolean.TYPE || c==Boolean.class)
		      return Boolean.FALSE;
		 else 
			 if (c==Character.TYPE || c==Character.class)
		       return '\u0000';
		 else 
			if (c==Long.TYPE || c==Long.class)
				return 0L;
		else 
			if (c==Integer.TYPE || c==Integer.class)
				return 0;	
		else 
			if(c==Short.TYPE || c==Short.class)
				return (short) 0;
		else 
			if (c==Byte.TYPE || c==Byte.class)
				return (byte) 0;
		else 
			if(c==Float.TYPE || c==Float.class)
				return 0f;
		else 
			if(c==Double.TYPE || c==Double.class)
				return 0d;
		else
			if (c==Byte.TYPE || c==Byte.class)
				return (byte) 0;
		 else
			 return null;
	}
}
