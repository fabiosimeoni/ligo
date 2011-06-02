/**
 * 
 */
package org.ligo.patterns;

import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.core.data.LigoData;
import org.ligo.core.data.impl.NamedData;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class DataPattern {
	
	private LigoPattern pattern;
	private QName name;
	
	/**
	 * 
	 */
	public DataPattern(QName name, LigoPattern pattern) {
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
