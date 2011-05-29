/**
 * 
 */
package org.ligo.lab.core.annotations;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.impl.NamedBinder;
import org.ligo.lab.core.impl.ParameterContext;

/**
 * @author Fabio Simeoni
 *
 */
public interface AnnotationProcessor {

	NamedBinder binderFor(ParameterContext context, Environment env);
}
