package org.ligo.lab.dsl;

import org.ligo.lab.binders.TypeBinderFactory;
import org.ligo.lab.binders.DataBinder;
import org.ligo.lab.binders.DataBinderFactory;
import org.ligo.lab.binders.TypeBinder;

class WithClauseImpl<TYPE> implements WithClause<TYPE> {
	
	ClauseContext<TYPE,?,?> ctxt;

	public WithClauseImpl(ClauseContext<TYPE,?,?> c) {
		ctxt = c;
	}
	
	/**{@inheritDoc}*/
	@Override
	public <IN> EndClause<TYPE,IN> with(TypeBinder<IN, TYPE> binder) {
		return new EndClauseImpl<TYPE,IN>(new ClauseContext<TYPE,IN,TYPE>(ctxt.type(),binder));
	}
	
	/**{@inheritDoc}*/
	@Override
	public <IN> EndClause<TYPE,IN> with(TypeBinderFactory<IN, TYPE> factory) {
		return with(factory.bind(ctxt.type()));
	}
	
	/**{@inheritDoc}*/
	@Override
	public <IN, OUT> AndClause<TYPE, IN, OUT> with(DataBinder<IN, OUT> binder) {
		
		ClauseContext<TYPE,IN,OUT> c = new ClauseContext<TYPE,IN,OUT>(ctxt.type(),binder);
		return new AndClauseImpl<TYPE, IN, OUT>(c);
	}

	/**{@inheritDoc}*/
	@Override
	public <IN, OUT> AndClause<TYPE, IN, OUT> with(DataBinderFactory<TYPE, IN, OUT> factory) {
		ClauseContext<TYPE, IN, OUT> c = new ClauseContext<TYPE, IN, OUT>(ctxt.type(), factory.bind(ctxt.type()));
		return new AndClauseImpl<TYPE, IN, OUT>(c);
	}
	
	/**{@inheritDoc}*/
	@Override
	public <IN> EndClause<TYPE, IN> with(DSLDefaults<IN> defaults) {
		return defaults.complete(this,ctxt);
	}
	
}