package org.ligo.lab.dsl;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.DataBinder;

public interface EndClause<TYPE,OUT> {
	
	<STREAM> Binder<STREAM,TYPE> and(DataBinder<STREAM,OUT> t);
	Binder<OUT,TYPE> build();
}