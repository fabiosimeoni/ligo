/**
 * 
 */
package org.ligo.core.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.ligo.core.binders.BindMode.*;
import static org.ligo.core.utils.Constants.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.ligo.core.binders.BindMode;


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
public @interface Bind {

	String value() default NONAME;
	
	String ns() default "";
	
	BindMode mode() default DEFAULT;

}
