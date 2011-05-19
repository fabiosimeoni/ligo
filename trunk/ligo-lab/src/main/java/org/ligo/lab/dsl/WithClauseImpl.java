package org.ligo.lab.dsl;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.BinderFactory;
import org.ligo.lab.binders.DataBinder;
import org.ligo.lab.binders.DataBinderFactory;

class WithClauseImpl<TYPE> implements WithClause<TYPE> {
	
	ClauseContext<TYPE,?,?> ctxt;

	public WithClauseImpl(ClauseContext<TYPE,?,?> c) {
		ctxt = c;
	}
	
	/**{@inheritDoc}*/
	@Override
	public <IN> EndClause<TYPE,IN> with(Binder<IN, TYPE> binder) {
		return new EndClauseImpl<TYPE,IN>(new ClauseContext<TYPE,IN,TYPE>(ctxt.boundtype(),binder));
	}
	
	/**{@inheritDoc}*/
	@Override
	public <IN> EndClause<TYPE,IN> with(BinderFactory<IN, TYPE> factory) {
		return with(factory.bind(ctxt.boundtype()));
	}
	
	/**{@inheritDoc}*/
	@Override
	public <IN, OUT> AndClause<TYPE, IN, OUT> with(DataBinder<IN, OUT> pattern) {
		
		ClauseContext<TYPE,IN,OUT> c = new ClauseContext<TYPE,IN,OUT>(ctxt.boundtype,pattern);
		return new AndClauseImpl<TYPE, IN, OUT>(c);
	}

	/**{@inheritDoc}*/
	@Override
	public <IN, OUT> AndClause<TYPE, IN, OUT> with(DataBinderFactory<TYPE, IN, OUT> translation) {
		ClauseContext<TYPE, IN, OUT> c = new ClauseContext<TYPE, IN, OUT>(ctxt.boundtype(), translation.bind(ctxt.boundtype));
		return new AndClauseImpl<TYPE, IN, OUT>(c);
	}
	
	
}