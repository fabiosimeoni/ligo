package org.ligo.lab.core.impl;

import javax.xml.namespace.QName;

import org.ligo.lab.core.TypeBinder;

public class NamedBinder {
	private QName name;
	private TypeBinder<?> binder;
	
	public NamedBinder(QName n,TypeBinder<?> b) {
		name=n;binder=b;
	}
	
	/**
	 * @return the name
	 */
	public QName name() {
		return name;
	}
	
	/**
	 * @return the binder
	 */
	public TypeBinder<?> binder() {
		return binder;
	}
}