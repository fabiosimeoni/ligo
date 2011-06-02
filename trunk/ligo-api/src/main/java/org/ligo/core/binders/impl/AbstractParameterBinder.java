/**
 * 
 */
package org.ligo.core.binders.impl;

import java.lang.reflect.Member;

import javax.xml.namespace.QName;

import org.ligo.core.binders.Environment;
import org.ligo.core.binders.TypeBinder;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractParameterBinder<M extends Member> implements ParameterBinder<M> {

	private final Environment env;
	
	private QName name;
	private TypeBinder<?> binder;
	
	/**{@inheritDoc}*/
	public AbstractParameterBinder(Environment e) {
		env=e;
	}
	
	void setBoundName(QName n) {
		name=n;
	}
	
	void setBinder(TypeBinder<?> b) {
		binder = b;
	}
	
	/**{@inheritDoc}*/
	@Override
	public QName boundName() {
		return name;
	}
	
	/**
	 * @return the env
	 */
	Environment environment() {
		return env;
	}
	
	/**{@inheritDoc}*/
	@Override
	public TypeBinder<?> binder() {
		return binder;
	}
	
}
