package org.ligo.dsl;

import org.ligo.binders.Binder;
import org.ligo.core.keys.Key;

public class ClauseContext<TYPE,IN> {
	
	private Binder<IN,TYPE> transform;
	
	private Key<TYPE> key;
	
	public ClauseContext(Key<TYPE> k) {
		this(k,null);
	}
	
	public ClauseContext(Key<TYPE> k, Binder<IN,TYPE> t) {
		key = k;
		transform=t;
	}
	
	public Key<TYPE> key() {
		return key;
	}
		
	public Binder<IN,TYPE> binding() {
		return transform;
	}
	
}