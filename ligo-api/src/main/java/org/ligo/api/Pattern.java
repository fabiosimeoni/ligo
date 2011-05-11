/**
 * 
 */
package org.ligo.api;


/**
 * @author Fabio Simeoni
 *
 */
public interface Pattern<MODEL,MATCH> {

	MATCH extract(MODEL d);
}
