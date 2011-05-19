/**
 * 
 */
package org.ligo.lab.types.impl;

import java.lang.reflect.Method;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * @author Fabio Simeoni
 *
 */
public class MethodDef extends AbstractMethodDef {

	private Method method;
	
	public MethodDef(Method m, List<QName> names) {
		super(names);
		method=m;
	}
	
	/**
	 * @return the method
	 */
	public Method method() {
		return method;
	}
	
}
