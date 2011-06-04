/**
 * 
 */
package org.ligo.data.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
		
		Set<QName> othernames = other.names();
		Set<QName> names = new HashSet<QName>(names());
		
		if (names == null) {
			if (othernames != null)
				return false;
		} else {
			for (QName name : othernames) {
				List<LigoData> ps = data(name);
				List<LigoData> otherps = other.data(name);
				if (ps ==null) {
					if (otherps!=null)
						return false;
				}
				else {
					if (!ps.equals(otherps))
						return false;
					else
						names.remove(name);
				}
			}
			if (!names.isEmpty())
				return false;
		}
		
		return true;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); 
		Iterator<QName> nit = names().iterator();
		while (nit.hasNext()) {
			QName name = nit.next();
			Iterator<LigoData> dit = data(name).iterator();
			while (dit.hasNext())
				b.append(name+"="+dit.next()+(dit.hasNext()?",":""));
			b.append(nit.hasNext()?",":"");
		}
		b.append("]");
		return b.toString();
	}
	
}
