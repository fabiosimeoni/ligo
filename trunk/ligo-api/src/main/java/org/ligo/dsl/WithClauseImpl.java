package org.ligo.dsl;

import org.ligo.core.Binder;
import org.ligo.core.BinderFactory;
import org.ligo.core.Environment;
import org.ligo.core.LigoFactory;
import org.ligo.data.LigoData;

class WithClauseImpl<TYPE> implements WithClause<TYPE> {
	
	ClauseContext<TYPE,?> ctxt;

	public WithClauseImpl(ClauseContext<TYPE,?> c) {
		ctxt = c;
	}
	
	/**{@inheritDoc}*/
	@Override
	public <IN> AndClause<TYPE,IN> with(Binder<IN, TYPE> binder) {
		return new AndClauseImpl<TYPE,IN>(new ClauseContext<TYPE,IN>(ctxt.key(),binder));
	}
	
	/**{@inheritDoc}*/
	@Override
	public <IN> AndClause<TYPE,IN> with(BinderFactory<TYPE,IN, TYPE> factory) {
		return with(factory.binderFor(ctxt.key()));
	}
	
	/**{@inheritDoc}*/
	@Override
	public AndClause<TYPE,LigoData> in(Environment env) {
		return with(new LigoFactory<TYPE>(env));
	}
	
	/**{@inheritDoc}*/
	@Override
	public <IN> AndClause<TYPE,IN> with(DSLDefaults<IN> defaults) {
		return defaults.complete(this,ctxt);
	}
	
}