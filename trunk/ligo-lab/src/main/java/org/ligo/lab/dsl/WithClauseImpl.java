package org.ligo.lab.dsl;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.BinderFactory;

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
		return with(factory.bind(ctxt.key()));
	}
	
	/**{@inheritDoc}*/
	@Override
	public <IN> AndClause<TYPE,IN> with(DSLDefaults<IN> defaults) {
		return defaults.complete(this,ctxt);
	}
	
}