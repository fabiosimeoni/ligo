/**
 * 
 */
package org.ligo.patterns.impl;

import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.data.LigoData;
import org.ligo.data.impl.NamedData;
import org.ligo.patterns.api.DataPattern;
import org.ligo.patterns.api.LigoPattern;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class DefaultDataPattern implements DataPattern {
	
	private LigoPattern pattern;
	private QName name;
	
	/**
	 * 
	 */
	public DefaultDataPattern(QName name, LigoPattern pattern) {
		this.name=name;
		this.pattern=pattern;
	}
	
	/**
	 * @return the name
	 */
	public QName name() {
		return name;
	}
	
	/**
	 * @return the pattern
	 */
	public LigoPattern pattern() {
		return pattern;
	}
	
	public abstract List<NamedData> bind(List<LigoData> data);
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return name+":"+pattern;
		
	}
}
