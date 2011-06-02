package org.ligo.dsl;

import org.ligo.core.Binder;
import org.ligo.core.BinderFactory;

public interface AndClause<TYPE,IN> {
	
	<SOURCE> AndClause<TYPE,SOURCE> and(Binder<SOURCE,IN> transform);
	<SOURCE> AndClause<TYPE,SOURCE> and(BinderFactory<TYPE,SOURCE,IN> transformFactory);
	
	Binder<IN,TYPE> build();
}