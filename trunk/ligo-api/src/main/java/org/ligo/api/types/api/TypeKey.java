/**
 * 
 */
package org.ligo.api.types.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Fabio Simeoni
 *
 */
public final class TypeKey<T> {

	private Class<? extends T> type;
	private Annotation qualifier;
	private Map<TypeVariable<?>,Type> parameters;
	
	public TypeKey(Class<? extends T> t) {
		this(t,(Annotation)null);
	}
	
	public TypeKey(Class<? extends T> t, Annotation q) {
		this(t,q,(Map<TypeVariable<?>,Type>)null);
	}
	
	public TypeKey(Class<? extends T> t, Map<TypeVariable<?>,Type> tps) {
		this(t,null,tps);
	}
	
	public TypeKey(Class<? extends T> t, Annotation q, Map<TypeVariable<?>,Type> tps) {
		type=t;
		qualifier=q;
		parameters=tps;
	}
	
	public Class<? extends T> type() {
		return type;
	}
	
	public Annotation qualifier() {
		return qualifier;
	}
	
	public Map<TypeVariable<?>,Type> typeParameters() {
		return parameters;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(type.getSimpleName()).append(qualifier==null?"":","+qualifier.annotationType());
		if (parameters!=null) {
			Collection<Type> params = parameters.values();
			b.append("<");
			Iterator<Type> it = params.iterator();
			while (it.hasNext()) {
				Type t = it.next();
				if (t instanceof Class<?>)
					b.append(((Class<?>)t).getSimpleName());
				else if (t instanceof ParameterizedType)
					b.append(((ParameterizedType) t).getRawType().getClass().getSimpleName());
				else 
					b.append(t);
				if (it.hasNext())
					b.append(",");
			}
			b.append(">");
		}
		return b.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
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
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
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
