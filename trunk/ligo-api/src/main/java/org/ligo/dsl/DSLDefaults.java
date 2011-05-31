package org.ligo.dsl;



public interface DSLDefaults<IN> {

	<TYPE> AndClause<TYPE,IN> complete(WithClause<TYPE> clause, ClauseContext<TYPE,?> ctxt);
}