package org.ligo.lab.typebinders.kinds;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Facilities for programming over {@link Type}s.
 * 
 * @author Fabio Simeoni
 *
 */
public abstract class Kind<T extends Type> {

	public static enum KindValue {CLASS,GENERIC,TYPEVAR,GENERICARRAY}
	
	public static Class<?> CLASS(Kind<?> t) {
		return ((ClassKind) t).type();
	}
	
	public static ParameterizedType GENERIC(Kind<?> t) {
		return ((GenericKind)t).type();
	}
	
	public static GenericArrayType GENERICARRAY(Kind<?> t) {
		return ((GenericArrayKind)t).type();
	}
	
	public static TypeVariable<?> TYPEVAR(Kind<?> t) {
		return ((VarKind)t).type();
	}
	
	public static Kind<?> kindOf(Type t) {
		if (t instanceof Class<?>)
			return new ClassKind((Class<?>)t);
		else 
			if (t instanceof ParameterizedType)
				return new GenericKind((ParameterizedType)t);
		else
			if (t instanceof TypeVariable<?>)
				return new VarKind((TypeVariable<?>)t);
		else 
			if (t instanceof GenericArrayType)
				return new GenericArrayKind((GenericArrayType)t);
		else 
			throw new RuntimeException("unknown type "+t.getClass());
	}

	
	private T type;
	
	
	Kind(T t) {
		type=t;
	}
	
	abstract public KindValue value();
	
	T type() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Kind<?>))
			return false;
		Kind<?> other = (Kind<?>) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}