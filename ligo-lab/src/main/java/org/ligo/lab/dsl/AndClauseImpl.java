package org.ligo.lab.dsl;

import org.ligo.lab.binders.CompositeBinder;
import org.ligo.lab.binders.Binder;

class AndClauseImpl<TYPE,IN,OUT> implements AndClause<TYPE, IN, OUT> {
	
	ClauseContext<TYPE,IN,OUT> ctxt;
	
	/**
	 * 
	 */
	public AndClauseImpl(ClauseContext<TYPE, IN, OUT> c) {
		ctxt=c;
	}
	
	/**{@inheritDoc}*/
	@Override
	public EndClause<TYPE,IN> and(Binder<OUT, TYPE> binder) {
		CompositeBinder<IN, OUT, TYPE> ct = new CompositeBinder<IN, OUT, TYPE>(ctxt.transform(),binder); 
		return new EndClauseImpl<TYPE, IN>(new ClauseContext<TYPE, IN,TYPE>(ctxt.type(),ct));
	}
}