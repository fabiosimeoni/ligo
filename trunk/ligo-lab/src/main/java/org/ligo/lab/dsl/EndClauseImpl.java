/**
 * 
 */
package org.ligo.lab.dsl;

import org.ligo.lab.binders.CompositeBinder;
import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.DataBinder;

/**
 * @author Fabio Simeoni
 *
 */
public class EndClauseImpl<TYPE,IN> implements EndClause<TYPE,IN> {

	ClauseContext<TYPE,IN,TYPE> ctxt;
	
	/**
	 * 
	 */
	public EndClauseImpl(ClauseContext<TYPE,IN,TYPE> c) {
		ctxt=c;
	}
	
	/**{@inheritDoc}*/
	@Override
	public <STREAM> Binder<STREAM, TYPE> and(DataBinder<STREAM, IN> t) {
		CompositeBinder<STREAM, IN, TYPE> ct = new CompositeBinder<STREAM,IN,TYPE>(t,ctxt.transform()); 
		return new DelegateBinder<STREAM,TYPE>(ct);
	}
	
	/**{@inheritDoc}*/
	@Override
	public Binder<IN, TYPE> build() {
		return new DelegateBinder<IN,TYPE>(ctxt.transform());
	}

}
