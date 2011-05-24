/**
 * 
 */
package org.ligo.lab.typebinders;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.ligo.lab.typebinders.Bind.Mode.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Fabio Simeoni
 *
 */
@Documented
@Retention(RUNTIME)
@Target(value=PARAMETER)
/**
 * Identifies the data required for binding and the method and constructor parameters through which the 
 * data can be bound.
 */
public @interface Bind {

	public static enum Mode{STRICT,LAX,DEFAULT}
	
	String value();
	
	String ns() default "";
	
	Mode mode() default DEFAULT;
}
