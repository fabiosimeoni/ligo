/**
 * 
 */
package org.ligo.core.resolvers.api;

import java.lang.annotation.Annotation;

import org.ligo.core.keys.Key;
import org.ligo.core.keys.Literal;
import org.ligo.core.kinds.Kind;

/**
 * Resolves {@link Key}s into {@link Kind}s or their instances.
 * 
 * @author Fabio Simeoni
 *
 */
public interface ConfigurableResolver extends Resolver {
	
	/**
	 * Binds a class to a concrete implementation.
	 * @param <T> the class type
	 * @param clazz the class
	 * @param boundClass the bound implementation
	 */
	<T> void bind(Class<T> clazz,Class<? extends T> boundClass);
	
	/**
	 * Binds a class and an annotation class to a concrete implementation.
	 * @param <T> the class type.
	 * @param clazz the class
	 * @param qualifier the annotation class
	 * @param boundClass the concrete implementation
	 */
	<T> void bind(Class<T> clazz,Class<? extends Annotation> qualifier,Class<? extends T> boundClass);
	
	/**
	 * Binds a parametric type to a concrete implementation.
	 * @param <T> the parametric type
	 * @param lit the {@link Literal} of the parametric type
	 * @param boundClass the concrete implementation
	 */
	<T> void bind(Literal<T> lit,Class<? extends T> boundClass);
	
	/**
	 * Binds a parametric type to a concrete implementation.
	 * @param <T> the parametric type
	 * @param lit the {@link Literal} of the parametric type
	 * @param qualifier the annotation class
	 * @param boundClass the concrete implementation
	 */
	<T> void bind(Literal<T> lit,Class<? extends Annotation> qualifier,Class<? extends T> boundClass);
}
