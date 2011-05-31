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
public class BoundMethod extends AbstractBoundMember<Method> {

	private Method method;
	
	public BoundMethod(Method m, List<ParameterBinder<Method>> binders) {
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
