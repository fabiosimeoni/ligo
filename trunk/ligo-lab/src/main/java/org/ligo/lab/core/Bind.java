/**
 * 
 */
package org.ligo.lab.core;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.ligo.lab.core.Bind.Mode.*;

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
/**
 * Identifies the data required for binding and the method and constructor parameters through which the 
 * data can be bound.
 */
public @interface Bind {

	public static final String NULL=null;
	
	public static enum Mode{STRICT,LAX,DEFAULT}
	
	String value();
	
	String ns() default "";
	
	Mode mode() default DEFAULT;
}
