/**
 * 
 */
package org.ligo.core.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;

import org.ligo.core.Environment;

/**
 * @author Fabio Simeoni
 *
 */
public interface ParameterBinderProvider {

	//currently not used
	Class<? extends Annotation> matchingAnnotation();
	
	<M extends Member> ParameterBinder<M> binder(BoundParameter<M> param, Environment env);
}
