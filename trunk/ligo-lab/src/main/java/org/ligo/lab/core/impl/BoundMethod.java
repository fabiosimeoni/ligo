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
public class BoundMethod extends AbstractBoundMember {

	private Method method;
	
	public BoundMethod(Method m, List<ParameterBinder> binders) {
		super(binders);
		method=m;
	}
	
	/**
	 * @return the method
	 */
	public Method method() {
		return method;
	}
	
}
