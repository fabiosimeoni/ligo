/**
 * 
 */
package org.ligo.patterns.api;

import java.util.List;

import org.ligo.data.LigoData;
import org.ligo.patterns.impl.DefaultDataPattern;

/**
 * @author Fabio Simeoni
 *
 */
public interface ObjectPattern extends LigoPattern {

	/**
	 * @return the patternMap
	 */
	List<DefaultDataPattern> patterns();

	/**{@inheritDoc}*/
	LigoData bind(LigoData data);

}