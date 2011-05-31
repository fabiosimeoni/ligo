/**
 * 
 */
package org.ligo.core.impl;

import java.lang.reflect.Member;

import javax.xml.namespace.QName;

import org.ligo.core.TypeBinder;
import org.ligo.core.data.StructureProvider;

/**
 * @author Fabio Simeoni
 *
 */
public interface ParameterBinder<M extends Member> {

	QName boundName();
	TypeBinder<?> binder();
	Object bind(StructureProvider sp);
	
}
