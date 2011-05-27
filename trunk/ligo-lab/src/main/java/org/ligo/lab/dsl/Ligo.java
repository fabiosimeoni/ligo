package org.ligo.lab.dsl;

import static org.ligo.lab.core.Key.*;

import java.lang.annotation.Annotation;

import org.ligo.lab.core.Literal;

public class Ligo {

	public static <TYPE> WithClause<TYPE> bind(Class<TYPE> type) {
		return bind(type,null);
	}
	
	public static <TYPE> WithClause<TYPE> bind(Class<TYPE> type, Class<? extends Annotation> a) {
		return new WithClauseImpl<TYPE>(new ClauseContext<TYPE,Object>(get(type,a)));
	}
	
	public static <TYPE> WithClause<TYPE> bind(Literal<TYPE> type) {
		return bind(type,null);
	}
	
	public static <TYPE> WithClause<TYPE> bind(Literal<TYPE> type, Class<? extends Annotation> a) {
		return new WithClauseImpl<TYPE>(new ClauseContext<TYPE,Object>(get(type,a)));
	}
}