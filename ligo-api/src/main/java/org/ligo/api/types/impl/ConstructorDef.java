/**
 * 
 */
package org.ligo.api.types.impl;

import java.lang.reflect.Constructor;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * @author Fabio Simeoni
 *
 */
public class ConstructorDef<T> extends AbstractMethodDef {

	private Constructor<? extends T> constructor;
	
	public ConstructorDef(Constructor<? extends T> c, List<QName> names) {
		super(names);
		constructor=c;
	}
	
	Constructor<? extends T> constructor() {
		return constructor;
	}
}
