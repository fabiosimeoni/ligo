package org.ligo.lab.binders;

public interface Binder<IN,OUT> {
	
	OUT bind(IN in);
}