package org.ligo.lab.dsl;

import org.ligo.lab.binders.Binder;

class DelegateBinder<IN,TYPE> implements Binder<IN, TYPE> {
	
	Binder<IN,TYPE> pipe;
	/**
	 * 
	 */
	public DelegateBinder(Binder<IN,TYPE> p) {
		pipe=p;
	}
	public TYPE bind(IN data) {
		return pipe.bind(data);
	};
}