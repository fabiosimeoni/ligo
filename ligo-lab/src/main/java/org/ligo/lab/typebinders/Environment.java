/**
 * 
 */
package org.ligo.lab.typebinders;

import java.lang.reflect.TypeVariable;



/**
 * The environment in which {@link TypeBinder}s are generated and used.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Environment {

	/**
	 * Returns a binder for a given of a given type.
	 * @param <T> the type of the key
	 * @param key the key
	 * @return the binder
	 */
	<T> TypeBinder<T> binderFor(Key<T> key);

	/**
	 * Binds a type variable to a given binder.
	 * @param var the variable
	 * @param binder the binder
	 */
	void bindVariable(TypeVariable<?> var,TypeBinder<?> binder);

	/**
	 * Returns the {@link Resolver} used by the environment.
	 * @return the resolver.
	 */
	Resolver resolver();
	
	
}
