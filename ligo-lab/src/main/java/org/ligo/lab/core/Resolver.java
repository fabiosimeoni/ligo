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
	 * Resolves a key into one or more kinds, each of which may refine the key's.
	 * <p>
	 * Returns the key's kind (in a singleton list) if this has no refinements.
	 * @param key the key.
	 * @return the key's kind refinements.
	 */
	<T> List<Class<T>> resolve(Key<T> key);
	
	/**
	 * Resolves a key into an instance of a kind resolved by {@link #resolve(Key)},
	 * if this is a reifiable type.
	 *  
	 * @param <T> the key's type.
	 * @param key the key.
	 * @param args the arguments of a constructor of the kind.
	 * @return the instance
	 * @throws RuntimeException if the key's kind could not be refined into a reifiable type,
	 * or if the reifiable type could not be reified.
	 */
	<T> T resolve(Key<T> key, List<Object> args) throws RuntimeException;
}
