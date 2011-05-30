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

	private final Class<? extends T> clazz;

	public ClassKey(Class<? extends T> clazz) {
		this(clazz,null);
	}
	
	public ClassKey(Class<? extends T> clazz, Class<? extends Annotation> qualifier) {
		super(kindOf(clazz),qualifier);
		this.clazz=clazz;
	}
	
	/**{@inheritDoc}*/
	@Override
	public ClassKind kind() {
		return (ClassKind)super.kind(); //internally consistent
	}
	
	public Class<? extends T> classKey() {
		return clazz;
	}
	
	public ClassKey<T> unqualified() {
		return new ClassKey<T>(clazz);
	}
}
