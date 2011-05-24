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
public @interface Bind {

	public static enum Mode{STRICT,LAX}
	
	String value();
	
	String ns() default "";
	
	Mode mode() default STRICT;
}