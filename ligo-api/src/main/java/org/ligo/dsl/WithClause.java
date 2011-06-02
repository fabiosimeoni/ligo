package org.ligo.dsl;

import org.ligo.core.binders.Binder;
import org.ligo.core.binders.BinderFactory;
import org.ligo.core.binders.Environment;
import org.ligo.data.LigoData;

public interface WithClause<TYPE> {
	
	<IN> AndClause<TYPE,IN> with(Binder<IN,TYPE> typebinder);
	<IN> AndClause<TYPE,IN> with(BinderFactory<TYPE,IN,TYPE> factory);
	AndClause<TYPE,LigoData> in(Environment environment);
	
	<IN> AndClause<TYPE,IN> with(DSLDefaults<IN> defaults);
}

