package org.ligo.lab.binders;

public interface DataBinderFactory<TYPE,IN,OUT> extends Binder<Class<TYPE>,Binder<IN,OUT>> {
	
}