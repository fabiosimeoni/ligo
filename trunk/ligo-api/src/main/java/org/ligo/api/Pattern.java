/**
 * 
 */
package org.ligo.api;


/**
 * @author Fabio Simeoni
 *
 */
public interface Pattern<IN,OUT> {

	OUT extract(IN d);
}
