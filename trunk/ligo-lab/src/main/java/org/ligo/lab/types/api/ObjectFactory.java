/**
 * 
 */
package org.ligo.lab.types.api;

import java.lang.reflect.Constructor;

/**
 * @author Fabio Simeoni
 *
 */
public interface ObjectFactory {

	<T> T getInstance(TypeKey<T> key, Object[] args, Constructor<? extends T> constructor);
	<T> Class<? extends T> getType(TypeKey<T> key);
}
