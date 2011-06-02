/**
 * 
 */
package org.ligo.data.impl;

import org.ligo.data.LigoValue;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractLigoValue implements LigoValue {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((get() == null) ? 0 : get().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LigoValue))
			return false;
		LigoValue other = (LigoValue) obj;
		if (get() == null) {
			if (other.get() != null)
				return false;
		} else if (!get().equals(other.get()))
			return false;
		return true;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return get().toString();
	}
	
	
}
