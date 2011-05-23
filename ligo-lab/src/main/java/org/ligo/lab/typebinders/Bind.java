/**
 * 
 */
package org.ligo.lab.typebinders;

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
@Target(value=PARAMETER)
public @interface Bind {

	String value();
	String ns() default "";
}
