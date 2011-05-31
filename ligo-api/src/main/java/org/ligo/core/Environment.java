/**
 * 
 */
package org.ligo.core;

import org.ligo.core.keys.Key;




/**
 * The environment in which {@link TypeBinder}s are generated and used.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Environment {

	/**
	 * Returns a binder for a given parametric type.
	 * @param <T> the parametric type
	 * @param lit a {@link Literal} of the parametric type
	 * @return the binder
	 */
	<T> TypeBinder<T> binderFor(Literal<? extends T> lit);
	
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
	<T> TypeBinder<T> binderFor(Key<? extends T> key);

	/**
	 * Returns the {@link Resolver} used by the environment.
	 * @return the resolver.
	 */
	Resolver resolver();
	
}
