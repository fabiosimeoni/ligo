package org.ligo.lab.dsl;

import org.ligo.lab.binders.BinderFactory;
import org.ligo.lab.binders.CompositeBinder;
import org.ligo.lab.binders.Binder;

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
		return new AndClauseImpl<TYPE, SOURCE>(new ClauseContext<TYPE,SOURCE>(ctxt.type(),cb));
	}
	
	/**{@inheritDoc}*/
	@Override
	public <SOURCE> AndClause<TYPE, SOURCE> and(BinderFactory<Class<TYPE>, SOURCE, IN> transformFactory) {
		return and(transformFactory.bind(ctxt.type()));
	}
	
	/**{@inheritDoc}*/
	@Override
	public Binder<IN, TYPE> build() {
		return new DelegateBinder<IN, TYPE>(ctxt.binding());
	}
}