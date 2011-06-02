/**
 * 
 */
package org.ligo.patterns.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.ligo.data.LigoData;
import org.ligo.data.LigoObject;
import org.ligo.data.impl.DefaultLigoObject;
import org.ligo.data.impl.NamedData;
import org.ligo.patterns.api.DataPattern;
import org.ligo.patterns.api.ObjectPattern;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultObjectPattern extends AbstractLigoPattern implements ObjectPattern {

	Map<QName,DefaultDataPattern> patternMap = new HashMap<QName,DefaultDataPattern>();
	
	/**
	 * 
	 */
	public DefaultObjectPattern(List<DefaultDataPattern> patterns) {
		for (DefaultDataPattern dp : patterns) {
			if (patternMap.put(dp.name(),dp)!=null)
				throw new IllegalArgumentException(dp.name()+" repeated in "+patterns);
		}
	}
	
	/**
	 * @return the patternMap
	 */
	public List<DefaultDataPattern> patterns() {
		return new LinkedList<DefaultDataPattern>(patternMap.values());
	}
	
	/**{@inheritDoc}*/
	@Override
	public LigoData bind(LigoData data) {
		
		if (!(data instanceof LigoObject))
			throw new RuntimeException(this+" does not match value "+data);
		
		LigoObject object = (LigoObject) data; 
		
		
		//start assuming all edges are unmatched
		List<NamedData> matched = new LinkedList<NamedData>();
		
		Set<QName> names = object.names();
		
		for (DataPattern pattern : patternMap.values()) {
			
			//find potential matches
			List<LigoData> candidates = new LinkedList<LigoData>();
			for (QName name : names)
				if (match(pattern.name(),name))
					candidates.addAll(object.data(name));
					
			matched.addAll(pattern.bind(candidates));

		}

		return new DefaultLigoObject(matched);
	}
	
	//helper
	private boolean match(QName regexp,QName name) {
		return (name.getNamespaceURI().matches(regexp.getNamespaceURI()) && 
				name.getLocalPart().matches(regexp.getLocalPart()));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((patternMap == null) ? 0 : patternMap.hashCode());
		return result;
	}
	
	
	/**{@inheritDoc}*/
	@Override public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("[ ");
		for (DataPattern p : patternMap.values())
			b.append("("+p+") ");
		b.append("]");
		return b.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DefaultObjectPattern))
			return false;
		DefaultObjectPattern other = (DefaultObjectPattern) obj;
		if (patternMap == null) {
			if (other.patternMap != null)
				return false;
		} else if (!patternMap.equals(other.patternMap))
			return false;
		return true;
	}
}
