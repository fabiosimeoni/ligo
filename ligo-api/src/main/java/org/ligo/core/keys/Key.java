/**
 * 
 */
package org.ligo.core.keys;

import java.lang.annotation.Annotation;

import org.ligo.core.kinds.Kind;

/**
 * 
 * Identifies types for binding purposes.
 * 
 * 
 * @author Fabio Simeoni
 *
 */
public class Key<T> {

	private Kind<?> kind;
	private Class<? extends Annotation> qualifier;
	
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
	
	public Key<?> unqualified() {
		return new Key<Object>(kind,null);
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(kind).append(qualifier==null?"":","+qualifier.getSimpleName());
		return b.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
