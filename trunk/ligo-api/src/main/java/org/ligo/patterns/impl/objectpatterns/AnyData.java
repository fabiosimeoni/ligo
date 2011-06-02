/**
 * 
 */
package org.ligo.patterns.impl.objectpatterns;

import org.ligo.data.LigoData;
import org.ligo.patterns.impl.AbstractLigoPattern;

/**
 * @author Fabio Simeoni
 *
 */
public class AnyData extends AbstractLigoPattern {

	public static AnyData INSTANCE = new AnyData();
	
	private AnyData(){}
	
	/**{@inheritDoc}*/
	@Override
	public LigoData bind(LigoData data) {
		return data;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return "any";
	}
}
