/**
 * 
 */
package org.ligo.core.binders.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;

import org.ligo.core.binders.api.Environment;

/**
 * @author Fabio Simeoni
 *
 */
public interface ParameterBinderProvider {

	//currently not used
	Class<? extends Annotation> matchingAnnotation();
	
	<M extends Member> ParameterBinder<M> binder(BoundParameter<M> param, Environment env);
}
