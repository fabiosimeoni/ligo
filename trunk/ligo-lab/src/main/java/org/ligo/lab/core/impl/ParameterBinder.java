package org.ligo.lab.core.impl;

import javax.xml.namespace.QName;

import org.ligo.lab.core.TypeBinder;

public class ParameterBinder {
	private final QName name;
	private final TypeBinder<?> binder;
	private final ParameterContext context;
	
	public ParameterBinder(QName n,TypeBinder<?> b,ParameterContext c) {
		name=n;
		binder=b;
		context=c;
	}
	
	/**
	 * @return the name
	 */
	public QName boundName() {
		return name;
	}
	
	/**
	 * @return the binder
	 */
	public TypeBinder<?> binder() {
		return binder;
	}
	
	/**
	 * @return the bindingAnnotation
	 */
	public ParameterContext parameterContext() {
		return context;
	}
}