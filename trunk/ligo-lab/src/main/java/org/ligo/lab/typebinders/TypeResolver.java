/**
 * 
 */
package org.ligo.lab.typebinders;

import java.util.List;

import org.ligo.lab.typebinders.kinds.Kind;

/**
 * @author Fabio Simeoni
 *
 */
public interface TypeResolver {

	Kind<?> resolve(Key<?> key);
	<T> T resolve(Key<T> key, List<Object> args);
}
