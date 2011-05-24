package org.ligo.lab.binders;

/**
 * Binds an input to an output.
 * 
 * @author Fabio Simeoni
 *
 * @param <IN> the type of the input
 * @param <OUT> the type of the output
 */
public interface Binder<IN,OUT> {
	
	OUT bind(IN in);
}