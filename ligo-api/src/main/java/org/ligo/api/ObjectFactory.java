/**
 * 
 */
package org.ligo.api;

import java.util.List;

import org.ligo.api.types.api.TypeKey;

/**
 * @author Fabio Simeoni
 *
 */
public interface ObjectFactory {

	<T> T create(TypeKey<T> key, List<Object> args);
}
