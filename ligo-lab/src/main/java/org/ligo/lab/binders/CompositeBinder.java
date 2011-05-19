package org.ligo.lab.binders;

public class CompositeBinder<IN,INOUT,OUT> implements Binder<IN, OUT> {
	
	Binder<IN,INOUT> in;
	Binder<INOUT,OUT> out;
	
	/**
	 * 
	 */
	public CompositeBinder(Binder<IN,INOUT> i, Binder<INOUT,OUT> o) {
		in=i;
		out=o;
	}
	
	public OUT bind(IN data) {
		return out.bind(in.bind(data));
	};
}