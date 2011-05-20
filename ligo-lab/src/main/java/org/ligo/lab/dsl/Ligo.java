package org.ligo.lab.dsl;

public class Ligo {

	public static <TYPE> WithClause<TYPE> bind(Class<TYPE> type) {
		return new WithClauseImpl<TYPE>(new ClauseContext<TYPE,Object>(type));
	}
}