/**
 * 
 */
package org.ligo.core.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * @author Fabio Simeoni
 *
 */
@Documented
@Retention(RUNTIME)
@Target(value={CONSTRUCTOR,METHOD,PARAMETER})
@BindingAnnotation
/**
 * Identifies the data required for binding and the method and constructor parameters through which the 
 * data can be bound.
 */
public @interface BindConstant {}
