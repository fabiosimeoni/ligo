/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import java.util.List;

import javax.xml.namespace.QName;


/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractMethodDef {

	private List<QName> names;
	
	public AbstractMethodDef(List<QName> n) {
		names = n;
	}
	
	/**
	 * 
	 */
	public List<QName> names() {
		return names;
	}
	
}
