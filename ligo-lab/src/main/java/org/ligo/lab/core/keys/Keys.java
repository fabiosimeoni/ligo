/**
 * 
 */
package org.ligo.lab.core.keys;

import static org.ligo.lab.core.kinds.Kind.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.ligo.lab.core.Literal;
import org.ligo.lab.core.kinds.Kind;

/**
 * @author Fabio Simeoni
 *
 */
public class Keys {

	
	public static <T> ClassKey<T> newKey(Class<? extends T> clazz) {
		return new ClassKey<T>(clazz);
	}
	
	public static <T> ClassKey<T> newKey(Class<? extends T> clazz, Class<? extends Annotation> qualifier) {
		return new ClassKey<T>(clazz,qualifier);
	}
	
	public static <T> Key<T> newKey(Literal<T> t) {
		return newKey(t,null);
	}
	
	public static <T> Key<T> newKey(Literal<T> t, Class<? extends Annotation> a) {
		return new Key<T>(kindOf(t.type()),a);
	}
	
	public static Key<?> newKey(Type t) {
		return newKey(t,null);
	}
	
	public static Key<?> newKey(Type t, Class<? extends Annotation> a) {
		return new Key<Object>(kindOf(t),a);
	}
	
	public static Key<?> newKey(Kind<?> k) {
		return new Key<Object>(k,null);
	}
}
