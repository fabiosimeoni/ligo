/**
 * 
 */
package org.ligo.api.configuration;

import org.ligo.api.Binder;
import org.ligo.api.PatternFactory;

/**
 * @author Fabio Simeoni
 *
 */
public interface LigoContext<MODEL,MATCH> {

	Class<? extends MODEL> modelType();
	Class<? extends MATCH> matchType();
	
	PatternFactory<MODEL, MATCH> patternFactory();
	Binder<MATCH> binder();
	
}
