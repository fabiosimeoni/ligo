package org.ligo.lab.dsl;

import org.ligo.lab.binders.Binder;

public class ClauseContext<T,IN,OUT> {
	
	private Binder<IN,OUT> transform;
	
	private Class<T> type;
	
	
	public ClauseContext(Class<T> t) {
		type = t;
	}
	
	public ClauseContext(Class<T>type, Binder<IN, OUT> t) {
		this(type);
		transform=t;
	}
	
	public Class<T> type() {
		return type;
	}
	
	public Binder<IN, OUT> transform() {
		return transform;
	}
	
}