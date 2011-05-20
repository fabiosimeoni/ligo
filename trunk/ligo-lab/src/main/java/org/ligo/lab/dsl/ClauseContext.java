package org.ligo.lab.dsl;

import org.ligo.lab.binders.Binder;

public class ClauseContext<TYPE,IN> {
	
	private Binder<IN,TYPE> transform;
	
	private Class<TYPE> type;
	
	
	public ClauseContext(Class<TYPE> t) {
		type = t;
	}
	
	public ClauseContext(Class<TYPE>type, Binder<IN,TYPE> t) {
		this(type);
		transform=t;
	}
	
	public Class<TYPE> type() {
		return type;
	}
	
	public Binder<IN,TYPE> binding() {
		return transform;
	}
	
}