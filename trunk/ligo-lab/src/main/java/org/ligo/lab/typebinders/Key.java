/**
 * 
 */
package org.ligo.lab.typebinders;

import static org.ligo.lab.typebinders.kinds.Kind.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.ligo.lab.typebinders.kinds.Kind;

/**
 * 
 * Identifies types for binding purposes.
 * 
 * 
 * @author Fabio Simeoni
 *
 */
public final class Key<T> {

	private Kind<?> kind;
	private Class<? extends Annotation> qualifier;
	
	public static <T> Key<T> get(Class<T> t) {
		return get(t,null);
	}
	
	public static <T> Key<T> get(Class<T> t, Class<? extends Annotation> a) {
		return new Key<T>(kindOf(t),a);
	}
	
	public static <T> Key<T> get(TypeLiteral<T> t) {
		return get(t,null);
	}
	
	public static <T> Key<T> get(TypeLiteral<T> t, Class<? extends Annotation> a) {
		return new Key<T>(kindOf(t.type()),a);
	}
	
	public static Key<?> get(Type t) {
		return get(t,null);
	}
	
	@SuppressWarnings("unchecked")
	public static Key<?> get(Type t, Class<? extends Annotation> a) {
		return new Key(kindOf(t),a);
	}
	
	Key(Kind<?> k,Class<? extends Annotation> a) {
		kind=k;
		qualifier=a;
	}
	
	public Kind<?> kind() {
		return kind;
	}
	
	public Class<? extends Annotation> qualifier() {
		return qualifier;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(kind).append(qualifier==null?"":","+qualifier.getSimpleName());
//		if (parameters!=null) {
//			Collection<Type> params = parameters.values();
//			b.append("<");
//			Iterator<Type> it = params.iterator();
//			while (it.hasNext()) {
//				Type t = it.next();
//				if (t instanceof Class<?>)
//					b.append(((Class<?>)t).getSimpleName());
//				else if (t instanceof ParameterizedType)
//					b.append(((ParameterizedType) t).getRawType().getClass().getSimpleName());
//				else 
//					b.append(t);
//				if (it.hasNext())
//					b.append(",");
//			}
//			b.append(">");
//		}
		return b.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result
//				+ ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result
				+ ((qualifier == null) ? 0 : qualifier.hashCode());
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Key<?>))
			return false;
		Key<?> other = (Key<?>) obj;
//		if (parameters == null) {
//			if (other.parameters != null)
//				return false;
//		} else if (!parameters.equals(other.parameters))
//			return false;
		if (qualifier == null) {
			if (other.qualifier != null)
				return false;
		} else if (!qualifier.equals(other.qualifier))
			return false;
		if (kind == null) {
			if (other.kind != null)
				return false;
		} else if (!kind.equals(other.kind))
			return false;
		return true;
	}


}
