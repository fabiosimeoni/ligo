package org.ligo.xml;

import org.ligo.core.annotations.Bind;



class Generic<A> {
	
	final A a;
	
	@Bind("g")
	Generic(A a) {
		this.a=a;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Generic<?>))
			return false;
		Generic<?> other = (Generic<?>) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		return true;
	};
}