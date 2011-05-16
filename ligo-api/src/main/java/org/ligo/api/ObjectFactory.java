/**
 * 
 */
package org.ligo.api;

import java.lang.reflect.Constructor;

import org.ligo.api.types.api.TypeKey;

/**
 * @author Fabio Simeoni
 *
 */
public interface ObjectFactory {

	<T> T getInstance(TypeKey<T> key, Object[] args, Constructor<? extends T> constructor);
	<T> Class<? extends T> getType(TypeKey<T> key);
}
