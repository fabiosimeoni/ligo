/**
 * 
 */
package org.ligo.api;

/**
 * @author Fabio Simeoni
 *
 */
public interface ProjectionContext<MODEL,MATCH> {

	Class<? extends MODEL> modelType();
	Class<? extends MATCH> matchType();
	
	PatternFactory<MODEL, MATCH> patternFactory();
	Binder<MATCH> binder();
	
}
