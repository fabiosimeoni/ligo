/**
 * 
 */
package org.ligo.api.types.impl;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author Fabio Simeoni
 *
 */
public class ConstructorDef<T> extends AbstractMethodDef {

	private Constructor<? extends T> constructor;
	
	public ConstructorDef(Constructor<? extends T> c, List<String> names) {
		super(names);
		constructor=c;
	}
	
	Constructor<? extends T> constructor() {
		return constructor;
	}
}
