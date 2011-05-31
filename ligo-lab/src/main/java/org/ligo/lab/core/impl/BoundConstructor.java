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
public class BoundConstructor extends AbstractBoundMember {

	private Constructor<?> constructor;
	
	public BoundConstructor(Constructor<?> c, List<ParameterBinder> names) {
		super(names);
		constructor=c;
	}
	
	Constructor<?> constructor() {
		return constructor;
	}
}
