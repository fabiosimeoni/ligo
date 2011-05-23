package org.ligo.lab.dsl;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.BinderFactory;

public interface AndClause<TYPE,IN> {
	
	<SOURCE> AndClause<TYPE,SOURCE> and(Binder<SOURCE,IN> transform);
	<SOURCE> AndClause<TYPE,SOURCE> and(BinderFactory<TYPE,SOURCE,IN> transformFactory);
	
	Binder<IN,TYPE> build();
}