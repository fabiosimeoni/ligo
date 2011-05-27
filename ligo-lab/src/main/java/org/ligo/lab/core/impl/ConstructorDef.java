/**
 * 
 */
package org.ligo.lab.core.impl;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author Fabio Simeoni
 *
 */
public class ConstructorDef extends AbstractMethodDef {

	private Constructor<?> constructor;
	
	public ConstructorDef(Constructor<?> c, List<NamedBinder> names) {
		super(names);
		constructor=c;
	}
	
	Constructor<?> constructor() {
		return constructor;
	}
}
