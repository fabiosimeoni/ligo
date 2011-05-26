/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.lab.typebinders.TypeBinder;


/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractMethodDef {

	public static class NamedBinder {
		QName name;
		TypeBinder<?> binder;
		public NamedBinder(QName n,TypeBinder<?> b) {
			name=n;binder=b;
		}
	}
	
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
