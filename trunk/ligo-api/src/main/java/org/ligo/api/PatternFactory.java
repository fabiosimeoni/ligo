/**
 * 
 */
package org.ligo.api;


/**
 * @author Fabio Simeoni
 *
 */
public interface PatternFactory<MODEL,MATCH> {

	Pattern<MODEL,MATCH> generate(Class<?> o);
}
