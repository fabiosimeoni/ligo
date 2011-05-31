/**
 * 
 */
package org.ligo.lab.core.impl;

import java.lang.reflect.Member;
import java.util.List;


/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractBoundMember<M extends Member> {

private List<ParameterBinder<M>> binders;
	
	public AbstractBoundMember(List<ParameterBinder<M>> binders) {
		this.binders = binders;
	}
	
	/**
	 * 
	 */
	public List<ParameterBinder<M>> parameterBinders() {
		return binders;
	}
	
}
