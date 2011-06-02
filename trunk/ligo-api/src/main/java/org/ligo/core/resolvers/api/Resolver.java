/**
 * 
 */
package org.ligo.core.resolvers.api;

import java.util.List;

import org.ligo.core.keys.Key;
import org.ligo.core.kinds.Kind;

/**
 * Resolves {@link Key}s into {@link Kind}s or their instances.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Resolver {
	
	/**
	 * Resolves a key into one or more classes, each of which may refine the key's kind.
	 * <data>
	 * Returns the key's class (in a singleton list) if this has no refinements.
	 * @param key the key.
	 * @return the key's kind refinements.
	 */
	<T> List<Class<? extends T>> resolve(Key<T> key);
	
	/**
	 * Instantiates into a class with given parameters.
	 *  
	 * @param <T> the class type
	 * @param clazz the class
	 * @param args the arguments of a constructor of the class
	 * @return the instance
	 * @throws RuntimeException if the class could not be instantiated from the parameters.
	 */
	<T> T resolve(Class<T> clazz, List<? extends Object> args) throws RuntimeException;
	
}
