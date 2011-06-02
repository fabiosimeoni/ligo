/**
 * 
 */
package org.ligo.core.data.impl;

import static java.util.Arrays.*;

import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.core.data.LigoData;
import org.ligo.core.data.LigoObject;
import org.ligo.core.data.LigoValue;

/**
 * @author Fabio Simeoni
 *
 */
public class DataBuilders {

	public static LigoObject o(NamedData ...data) {
		return new DefaultLigoObject(new LinkedList<NamedData>(asList(data)));
	}
	
	public static List<LigoData> d(LigoData ...data) {
		return asList(data);
	}
	
	public static LigoValue v(Object v) {
		return new DefaultLigoValue(v);
	}
	
	public static NamedData n(QName name, LigoData data) {
		return new NamedData(name, data);
	}
	
	public static NamedData n(QName label, Object value) {
		return new NamedData(label, v(value));
	}
	
	public static NamedData n(String label, LigoData data) {
		return new NamedData(new QName(label), data);
	}
	
	public static NamedData n(String label, Object value) {
		return new NamedData(new QName(label), v(value));
	}
}
