/**
 * 
 */
package org.ligo.lab.core;

import org.ligo.lab.core.keys.Key;




/**
 * The environment in which {@link TypeBinder}s are generated and used.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Environment {

	/**
	 * Returns a binder for a given class.
	 * @param <T> the class type
	 * @param clazz the class
	 * @return the binder
	 */
	<T> TypeBinder<T> binderFor(Class<? extends T> clazz);
	
	/**
	 * Returns a binder for a given key of a given type.
	 * @param <T> the type of the key
	 * @param key the key
	 * @return the binder
	 */
	<T> TypeBinder<T> binderFor(Key<T> key);

	/**
	 * Returns the {@link Resolver} used by the environment.
	 * @return the resolver.
	 */
	Resolver resolver();
	
	
}
