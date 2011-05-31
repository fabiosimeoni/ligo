package org.ligo.dsl;

import org.ligo.binders.Binder;
import org.ligo.binders.BinderFactory;

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
	public <IN> AndClause<TYPE,IN> with(DSLDefaults<IN> defaults) {
		return defaults.complete(this,ctxt);
	}
	
}