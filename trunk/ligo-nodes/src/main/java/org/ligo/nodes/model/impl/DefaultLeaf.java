/**
 * 
 */
package org.ligo.nodes.model.impl;

import org.ligo.api.data.DataProvider;
import org.ligo.api.data.ValueProvider;
import org.ligo.nodes.model.api.Leaf;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultLeaf implements Leaf {

	String value;
	
	/**
	 * 
	 */
	public DefaultLeaf(String v) {
		value=v;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String value() {
		return value;
	}

	/**{@inheritDoc}*/
	@Override
	public Leaf cloneNode() {
		return new DefaultLeaf(value);
	}
	
	/**{@inheritDoc}*/
	@Override
	public DataProvider provider() {
		return new ValueProvider() {
			public <T> T get(Class<T> expected) {
				Object outcome=null;
				if (expected.equals(String.class))
					outcome=value;
				else if (expected.equals(Integer.class))
					outcome=Integer.valueOf(value);
				else if (expected.equals(Boolean.class))
					outcome=Boolean.valueOf(value);
				//TODO continue
				return (outcome==null || !expected.isAssignableFrom(outcome.getClass()))?null:expected.cast(outcome);
			}
			public String toString() {
				return DefaultLeaf.this.toString();
			}
		};
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return value();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DefaultLeaf))
			return false;
		DefaultLeaf other = (DefaultLeaf) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	

}
