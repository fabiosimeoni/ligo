/**
 * 
 */
package org.ligo.lab.typebinders;

import java.util.List;

import org.ligo.lab.typebinders.kinds.Kind;

/**
 * Resolves {@link Key}s into {@link Kind}s or instances.
 * 
 * @author Fabio Simeoni
 *
 */
public interface TypeResolver {
	
	/**
	 * Resolves a key into a kind that may refine the key's.
	 * <p>
	 * Returns the key's kind if no refinement is possible.
	 * @param key the key.
	 * @return the kind.
	 */
	Kind<?> resolve(Key<?> key);
	
	/**
	 * Resolves a key into an instance the kind resolved by {@link #resolve(Key)},
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
