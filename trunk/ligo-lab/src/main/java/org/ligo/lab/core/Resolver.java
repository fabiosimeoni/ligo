/**
 * 
 */
package org.ligo.lab.core;

import java.util.List;

import org.ligo.lab.core.keys.Key;
import org.ligo.lab.core.kinds.Kind;

/**
 * Resolves {@link Key}s into {@link Kind}s or their instances.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Resolver {
	
	/**
	 * Resolves a key into one or more classes, each of which may refine the key's kind.
	 * <p>
	 * Returns the key's class (in a singleton list) if this has no refinements.
	 * @param key the key.
	 * @return the key's kind refinements.
	 */
	List<Class<?>> resolve(Key<?> key);
	
	/**
	 * Instantiates into a class with given parameters.
	 *  
	 * @param <T> the class type
	 * @param clazz the class
	 * @param args the arguments of a constructor of the class
	 * @return the instance
	 * @throws RuntimeException if the class could not be instantiated from the parameters.
	 */
	<T> T resolve(Class<T> key, List<? extends Object> args) throws RuntimeException;
	
}
