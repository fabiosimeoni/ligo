/**
 * 
 */
package org.ligo.lab.core.annotations;

import java.lang.reflect.Member;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.impl.ParameterBinder;
import org.ligo.lab.core.impl.ParameterContext;

/**
 * @author Fabio Simeoni
 *
 */
public interface AnnotationProcessor {

	<M extends Member> ParameterBinder<M> binderFor(ParameterContext<M> context, Environment env);
}
