package org.ligo.lab.dsl;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.BinderFactory;
import org.ligo.lab.binders.CompositeBinder;

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
		CompositeBinder<SOURCE, IN, TYPE> cb = new CompositeBinder<SOURCE, IN, TYPE>(transform, ctxt.binding()); 
		return new AndClauseImpl<TYPE, SOURCE>(new ClauseContext<TYPE,SOURCE>(ctxt.key(),cb));
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