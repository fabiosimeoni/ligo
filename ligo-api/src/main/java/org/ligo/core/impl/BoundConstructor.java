/**
 * 
 */
package org.ligo.core.impl;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author Fabio Simeoni
 *
 */
public class BoundConstructor extends AbstractBoundMember<Constructor<?>> {

	private Constructor<?> constructor;
	
	public BoundConstructor(Constructor<?> c, List<ParameterBinder<Constructor<?>>> names) {
		super(names);
		constructor=c;
	}
	
	Constructor<?> constructor() {
		return constructor;
	}
}
