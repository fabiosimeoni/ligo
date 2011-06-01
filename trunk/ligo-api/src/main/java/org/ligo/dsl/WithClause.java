package org.ligo.dsl;

import org.ligo.binders.Binder;
import org.ligo.binders.BinderFactory;
import org.ligo.core.Environment;
import org.ligo.core.data.LigoProvider;

public interface WithClause<TYPE> {
	
	<IN> AndClause<TYPE,IN> with(Binder<IN,TYPE> typebinder);
	<IN> AndClause<TYPE,IN> with(BinderFactory<TYPE,IN,TYPE> factory);
	AndClause<TYPE,LigoProvider> in(Environment environment);
	
	<IN> AndClause<TYPE,IN> with(DSLDefaults<IN> defaults);
}

