package org.ligo.xml;

import org.ligo.core.annotations.Bind;

public class Dependency {
	
	final String p;
	
	@Bind("p") 
	Dependency(String s) {
		p=s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Dependency))
			return false;
		Dependency other = (Dependency) obj;
		if (p == null) {
			if (other.p != null)
				return false;
		} else if (!p.equals(other.p))
			return false;
		return true;
	}
}