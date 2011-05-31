/**
 * 
 */
package org.ligo.core.keys;

import static org.ligo.core.kinds.Kind.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.ligo.core.Literal;
import org.ligo.core.kinds.Kind;

/**
 * @author Fabio Simeoni
 *
 */
public class Keys {

	
	public static <T> Key<T> newKey(Class<? extends T> clazz) {
		return newKey(clazz,null);
	}
	
	public static <T> Key<T> newKey(Class<? extends T> clazz, Class<? extends Annotation> qualifier) {
		return new Key<T>(kindOf(clazz),qualifier);
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
		return newKey(kindOf(t),a);
	}
	
	public static Key<?> newKey(Kind<?> k) {
		return newKey(k,null);
	}
	
	public static Key<?> newKey(Kind<?> k, Class<? extends Annotation> a) {
		return new Key<Object>(k,null);
	}
}
