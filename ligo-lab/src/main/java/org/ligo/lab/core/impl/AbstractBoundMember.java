/**
 * 
 */
package org.ligo.lab.core.impl;

import java.util.List;


/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractBoundMember {

private List<ParameterBinder> binders;
	
	public AbstractBoundMember(List<ParameterBinder> binders) {
		this.binders = binders;
	}
	
	/**
	 * 
	 */
	public List<ParameterBinder> parameterBinders() {
		return binders;
	}
	
}
