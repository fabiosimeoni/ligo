package org.ligo.lab.binders;

/**
 * A {@link Binder} that combines the bindings of two {@link Binder}s.
 * 
 * @author Fabio Simeoni
 *
 * @param <IN> the input type of the first binder
 * @param <INOUT> the output type of the first binder and the output type of the second binder
 * @param <OUT> the output type of the second binder
 */
public class CompositeBinder<IN,INOUT,OUT> implements Binder<IN, OUT> {
	
	private Binder<IN,INOUT> first;
	private Binder<INOUT,OUT> second;
	
	/**
	 * Creates an instance from two type-compatible binders.
	 * @param f the first binder.
	 * @param s the second binder.
	 */
	public CompositeBinder(Binder<IN,INOUT> f, Binder<INOUT,OUT> s) {
		first=f;
		second=s;
	}
	
	/**{@inheritDoc}*/
	public OUT bind(IN data) {
		return second.bind(first.bind(data));
	};
}