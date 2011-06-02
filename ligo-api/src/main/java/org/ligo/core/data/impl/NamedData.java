/**
 * 
 */
package org.ligo.core.data.impl;

import javax.xml.namespace.QName;

import org.ligo.core.data.LigoData;

/**
 * @author Fabio Simeoni
 *
 */
public class NamedData {

	private final QName name;
	private final LigoData data;
	
	/**
	 * @param name
	 * @param data
	 */
	public NamedData(QName name, LigoData data) {
		this.name = name;
		this.data = data;
	}
	
	/**
	 * @return the name
	 */
	public QName name() {
		return name;
	}
	
	/**
	 * @return the data
	 */
	public LigoData data() {
		return data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NamedData))
			return false;
		NamedData other = (NamedData) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
