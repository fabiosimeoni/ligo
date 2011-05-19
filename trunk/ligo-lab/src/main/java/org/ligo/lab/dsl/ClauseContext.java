package org.ligo.lab.dsl;

import org.ligo.lab.binders.Binder;

class ClauseContext<T,IN,OUT> {
	
	Binder<IN,OUT> transform;
	
	Class<T> boundtype;
	/**
	 * 
	 */
	public ClauseContext(Class<T>type) {
		boundtype = type;
	}
	
	public ClauseContext(Class<T>type, Binder<IN, OUT> t) {
		this(type);
		transform=t;
	}
	
	public Class<T> boundtype() {
		return boundtype;
	}
	
	public Binder<IN, OUT> transform() {
		return transform;
	}
	
}