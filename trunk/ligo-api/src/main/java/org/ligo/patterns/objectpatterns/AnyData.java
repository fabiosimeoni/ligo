/**
 * 
 */
package org.ligo.patterns.objectpatterns;

import org.ligo.data.LigoData;
import org.ligo.patterns.LigoPattern;

/**
 * @author Fabio Simeoni
 *
 */
public class AnyData extends LigoPattern {

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
