package org.ligo.lab.binders;

/**
 * Binds an input to an output.
 * 
 * @author Fabio Simeoni
 *
 * @param <IN> the input type
 * @param <OUT> the output type
 */
public interface Binder<IN,OUT> {
	
	/**
	 * Binds an input to an output.
	 * @param i the input
	 * @return the output
	 */
	OUT bind(IN i);
}