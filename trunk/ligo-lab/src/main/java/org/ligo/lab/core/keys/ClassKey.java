/**
 * 
 */
package org.ligo.lab.core.keys;

import static org.ligo.lab.core.kinds.Kind.*;

import java.lang.annotation.Annotation;

import org.ligo.lab.core.kinds.ClassKind;

/**
 * @author Fabio Simeoni
 *
 */
public class ClassKey<T> extends Key<T> {


	
	ClassKey(Class<?> clazz) {
		this(clazz,null);
	}
	
	ClassKey(Class<?> clazz, Class<? extends Annotation> qualifier) {
		super(kindOf(clazz),qualifier);
	}
	
	/**{@inheritDoc}*/
	@Override
	public ClassKind kind() {
		return (ClassKind)super.kind(); //internally consistent
	}
	
	public ClassKey<T> unqualified() {
		return new ClassKey<T>(kind().toClass());
	}
}
