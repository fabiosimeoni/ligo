/**
 * 
 */
package org.ligo.core.annotations;

import java.lang.reflect.Member;

import org.ligo.core.Environment;
import org.ligo.core.impl.ParameterBinder;
import org.ligo.core.impl.ParameterContext;

/**
 * @author Fabio Simeoni
 *
 */
public interface AnnotationProcessor {

	<M extends Member> ParameterBinder<M> binderFor(ParameterContext<M> context, Environment env);
}
