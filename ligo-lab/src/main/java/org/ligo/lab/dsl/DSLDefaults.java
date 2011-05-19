package org.ligo.lab.dsl;



public interface DSLDefaults<IN> {

	<TYPE> EndClause<TYPE,IN> complete(WithClause<TYPE> clause, ClauseContext<TYPE,?,?> ctxt);
}