/**
 * 
 */
package org.ligo.data.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.ligo.data.LigoData;
import org.ligo.data.LigoObject;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractLigoObject implements LigoObject {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		Set<QName> names = names();
		if (names()!=null)
			for (QName name : names) {
				List<LigoData> ps = data(name);
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
				List<LigoData> ps = data(name);
				List<LigoData> otherps = other.data(name);
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
		Map<QName,LigoData> ps = new HashMap<QName,LigoData>();
		for (QName name : names())
			for (LigoData data : data(name))
				ps.put(name,data);
			
		return ps.toString();
	}
	
}
