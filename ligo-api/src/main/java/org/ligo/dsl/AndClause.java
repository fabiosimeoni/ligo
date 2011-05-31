package org.ligo.dsl;

import org.ligo.binders.Binder;
import org.ligo.binders.BinderFactory;

public interface AndClause<TYPE,IN> {
	
	<SOURCE> AndClause<TYPE,SOURCE> and(Binder<SOURCE,IN> transform);
	<SOURCE> AndClause<TYPE,SOURCE> and(BinderFactory<TYPE,SOURCE,IN> transformFactory);
	
	Binder<IN,TYPE> build();
}