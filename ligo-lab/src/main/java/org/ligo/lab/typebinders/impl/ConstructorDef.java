/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import java.lang.reflect.Constructor;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * @author Fabio Simeoni
 *
 */
public class ConstructorDef extends AbstractMethodDef {

	private Constructor<?> constructor;
	
	public ConstructorDef(Constructor<?> c, List<QName> names) {
		super(names);
		constructor=c;
	}
	
	Constructor<?> constructor() {
		return constructor;
	}
}
