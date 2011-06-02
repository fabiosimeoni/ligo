/**
 * 
 */
package org.ligo.core.binders.impl;

import java.lang.reflect.Member;

import javax.xml.namespace.QName;

import org.ligo.core.binders.TypeBinder;
import org.ligo.data.LigoObject;

/**
 * @author Fabio Simeoni
 *
 */
public interface ParameterBinder<M extends Member> {

	QName boundName();
	TypeBinder<?> binder();
	Object bind(LigoObject sp);
	
}
