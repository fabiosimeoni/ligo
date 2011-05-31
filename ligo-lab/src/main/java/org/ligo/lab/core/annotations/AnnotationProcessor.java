/**
 * 
 */
package org.ligo.lab.core.annotations;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.impl.ParameterBinder;
import org.ligo.lab.core.impl.ParameterContext;

/**
 * @author Fabio Simeoni
 *
 */
public interface AnnotationProcessor {

	ParameterBinder binderFor(ParameterContext context, Environment env);
}
