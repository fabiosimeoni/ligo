package org.ligo.dsl;

import static org.ligo.dsl.Ligo.*;

import org.ligo.core.Binder;
import org.ligo.core.BinderFactory;

class AndClauseImpl<TYPE,IN> implements AndClause<TYPE, IN> {
	
	ClauseContext<TYPE,IN> ctxt;
	
	/**
	 * 
	 */
	public AndClauseImpl(ClauseContext<TYPE,IN> c) {
		ctxt=c;
	}
	
	/**{@inheritDoc}*/
	@Override
	public <SOURCE> AndClause<TYPE,SOURCE> and(Binder<SOURCE, IN> transform) {
		return new AndClauseImpl<TYPE, SOURCE>(new ClauseContext<TYPE,SOURCE>(ctxt.key(),compose(transform,ctxt.binding())));
	}
	
	/**{@inheritDoc}*/
	@Override
	public <SOURCE> AndClause<TYPE, SOURCE> and(BinderFactory<TYPE, SOURCE, IN> transformFactory) {
		return and(transformFactory.binderFor(ctxt.key()));
	}
	
	/**{@inheritDoc}*/
	@Override
	public Binder<IN, TYPE> build() {
		return new DelegateBinder<IN, TYPE>(ctxt.binding());
	}
}