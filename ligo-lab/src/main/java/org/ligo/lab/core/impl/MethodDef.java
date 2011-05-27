/**
 * 
 */
package org.ligo.lab.core.impl;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Fabio Simeoni
 *
 */
public class MethodDef extends AbstractMethodDef {

	private Method method;
	
	public MethodDef(Method m, List<NamedBinder> names) {
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
