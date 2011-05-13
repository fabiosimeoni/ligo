/**
 * 
 */
package org.ligo.api.types.api;

import static java.lang.String.*;
import static java.util.Arrays.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Fabio Simeoni
 *
 */
public final class TypeKey<T> {

	private Class<? extends T> type;
	private Annotation qualifier;
	private List<Type> parameters = new LinkedList<Type>();
	
	public TypeKey(Class<? extends T> t) {
		this(t,null);
	}
	
	public TypeKey(Class<? extends T> t, Annotation q) {
		this(t,q,null);
	}
	
	public TypeKey(Class<? extends T> t, Annotation q, Type[] tps) {
		type=t;
		qualifier=q;
		if (tps!=null)
			parameters=asList(tps);
	}
	
	public Class<? extends T> type() {
		return type;
	}
	
	public Annotation qualifier() {
		return qualifier;
	}
	
	public List<Type> typeParameters() {
		return parameters;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return qualifier==null?type.getSimpleName():format("(%1s,%2s)",type.getSimpleName(),qualifier.annotationType().getSimpleName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((qualifier == null) ? 0 : qualifier.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TypeKey<?>))
			return false;
		TypeKey<?> other = (TypeKey<?>) obj;
		if (qualifier == null) {
			if (other.qualifier != null)
				return false;
		} else if (!qualifier.equals(other.qualifier))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
