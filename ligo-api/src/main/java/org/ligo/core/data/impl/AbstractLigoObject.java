/**
 * 
 */
package org.ligo.core.data.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.ligo.core.data.LigoObject;
import org.ligo.core.data.LigoProvider;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractLigoObject implements LigoObject {

	List<LigoProvider> get;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		Set<QName> names = names();
		if (names()!=null)
			for (QName name : names) {
				List<LigoProvider> ps = get(name);
				if (ps!=null)
					result = prime * result + ((ps == null) ? 0 : ps.hashCode());
				else
					result = prime * result;
		}
		else 	
			result = prime * result;
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LigoObject))
			return false;
		LigoObject other = (LigoObject) obj;
		if (names() == null) {
			if (other.names() != null)
				return false;
		} else 
			for (QName name : other.names()) {
				List<LigoProvider> ps = get(name);
				List<LigoProvider> otherps = other.get(name);
				if (ps ==null) {
					if (otherps!=null)
						return false;
				}
				else 
					return ps.equals(otherps);
		}
		return true;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		List<LigoProvider> ps = new ArrayList<LigoProvider>();
		for (QName name : names())
			ps.addAll(get(name));
		return ps.toString();
	}
	
}
