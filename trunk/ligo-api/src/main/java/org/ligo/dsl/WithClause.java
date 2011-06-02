package org.ligo.dsl;

import org.ligo.core.binders.api.Binder;
import org.ligo.core.binders.api.BinderFactory;
import org.ligo.core.binders.api.Environment;
import org.ligo.data.LigoData;

public interface WithClause<TYPE> {
	
	<IN> AndClause<TYPE,IN> with(Binder<IN,TYPE> typebinder);
	<IN> AndClause<TYPE,IN> with(BinderFactory<TYPE,IN,TYPE> factory);
	AndClause<TYPE,LigoData> in(Environment environment);
	
	<IN> AndClause<TYPE,IN> with(DSLDefaults<IN> defaults);
}

