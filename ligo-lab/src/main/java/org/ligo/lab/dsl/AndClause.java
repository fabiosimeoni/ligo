package org.ligo.lab.dsl;

import org.ligo.lab.binders.Binder;

public interface AndClause<TYPE,IN,OUT> {
	
	EndClause<TYPE,IN> and(Binder<OUT,TYPE> binder);
}