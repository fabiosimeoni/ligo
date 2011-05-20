package org.ligo.lab.dsl;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.DataBinder;
import org.ligo.lab.binders.DataBinderFactory;
import org.ligo.lab.binders.TypeBinderFactory;

public interface WithClause<TYPE> {
	
	<IN> EndClause<TYPE,IN> with(Binder<IN,TYPE> binder);
	<IN> EndClause<TYPE,IN> with(TypeBinderFactory<IN,TYPE> factory);
	
	<IN,OUT> AndClause<TYPE,IN,OUT> with(DataBinder<IN,OUT> binder);
	<IN,OUT> AndClause<TYPE,IN,OUT> with(DataBinderFactory<TYPE,IN,OUT> factory);
	
	<IN> EndClause<TYPE,IN> with(DSLDefaults<IN> defaults);
}

