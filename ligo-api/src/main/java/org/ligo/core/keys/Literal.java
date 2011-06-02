package org.ligo.core.keys;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class Literal<T> {
	
	private Type type;
	
	protected Literal() {
		type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public Type type() {
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
		if (!(obj instanceof Literal<?>))
			return false;
		Literal<?> other = (Literal<?>) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return "literalOf("+type.toString()+")";
	}

}