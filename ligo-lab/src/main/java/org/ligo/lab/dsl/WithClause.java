package org.ligo.lab.dsl;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.BinderFactory;

public interface WithClause<TYPE> {
	
	<IN> AndClause<TYPE,IN> with(Binder<IN,TYPE> typebinder);
	<IN> AndClause<TYPE,IN> with(BinderFactory<Class<TYPE>,IN,TYPE> factory);
	
	<IN> AndClause<TYPE,IN> with(DSLDefaults<IN> defaults);
}

