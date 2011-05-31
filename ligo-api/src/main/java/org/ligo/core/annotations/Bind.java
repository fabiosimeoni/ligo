/**
 * 
 */
package org.ligo.core.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.ligo.core.annotations.Bind.Mode.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.ligo.core.impl.BindingAdapter;


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

	public static enum Mode{STRICT,LAX,DEFAULT}
	
	String value();
	
	String ns() default "";
	
	Mode mode() default DEFAULT;
	
	@SuppressWarnings("unchecked")
	Class<? extends BindingAdapter> adapter() default BindingAdapter.class;

}
