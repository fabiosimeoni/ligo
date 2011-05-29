/**
 * 
 */
package org.ligo.lab.core.impl;

import java.util.List;


/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractMethodDef {


	
	private List<NamedBinder> binders;
	
	public AbstractMethodDef(List<NamedBinder> n) {
		binders = n;
	}
	
	/**
	 * 
	 */
	public List<NamedBinder> binders() {
		return binders;
	}
	
}
