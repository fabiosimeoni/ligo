/**
 * 
 */
package org.ligo.api.types.impl;

import java.util.List;


/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractMethodDef {

	private List<String> names;
	
	public AbstractMethodDef(List<String> n) {
		names = n;
	}
	
	/**
	 * 
	 */
	public List<String> names() {
		return names;
	}
	
}
