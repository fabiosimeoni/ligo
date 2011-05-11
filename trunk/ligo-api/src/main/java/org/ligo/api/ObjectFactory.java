/**
 * 
 */
package org.ligo.api;

import java.lang.annotation.Annotation;

/**
 * @author Fabio Simeoni
 *
 */
public interface ObjectFactory {

	<T> T create(Class<T> type, Annotation ... annotations);
}
