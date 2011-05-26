/**
 * 
 */
package org.ligo.lab.typebinders.utils;

/**
 * @author Fabio Simeoni
 *
 */
public class ReflectionUtils {

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
			return Character.class;	
	}
	
	public static Object defaultOf(Class<?> c) {
		
		 if (c==Boolean.TYPE)
		      return Boolean.FALSE;
		 else 
			 if (c==Character.TYPE)
		       return '\u0000';
		 else 
			 
			 if (c==Long.TYPE || 
				 c==Integer.TYPE || 
				 c==Short.TYPE || 
				 c==Byte.TYPE ||
				 c==Float.TYPE || 
				 c==Double.TYPE ||
				 c==Character.TYPE)
			 
				 	return (byte) 0;
		 else
			 return null;
	}
}
