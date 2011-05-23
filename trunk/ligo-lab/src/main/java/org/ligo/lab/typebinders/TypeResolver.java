/**
 * 
 */
package org.ligo.lab.typebinders;

import org.ligo.lab.typebinders.kinds.Kind;

/**
 * @author Fabio Simeoni
 *
 */
public interface TypeResolver {

	Kind<?> resolve(Key<?> key);
	
}
