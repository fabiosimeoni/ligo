package org.ligo.lab.dsl;

import java.lang.annotation.Annotation;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.core.Literal;
import org.ligo.lab.core.keys.Key;
import org.ligo.lab.core.keys.Keys;

public class Ligo {

	public static <TYPE> WithClause<TYPE> bind(Class<TYPE> type) {
		return bind(Keys.newKey(type));
	}
	
	public static <TYPE> WithClause<TYPE> bind(Class<TYPE> type, Class<? extends Annotation> a) {
		return bind(Keys.newKey(type,a));
	}
	
	public static <TYPE> WithClause<TYPE> bind(Literal<TYPE> type) {
		return bind(Keys.newKey(type));
	}
	
	public static <TYPE> WithClause<TYPE> bind(Literal<TYPE> type, Class<? extends Annotation> a) {
		return bind(Keys.newKey(type,a));
	}
	
	public static <TYPE> WithClause<TYPE> bind(Key<TYPE> type) {
		return new WithClauseImpl<TYPE>(new ClauseContext<TYPE,Object>(type));
	}
	
	// composition methods to connect 2-4 different binders 
	public static <IN,T,OUT> Binder<IN,OUT> compose(final Binder<IN,T> b1, final Binder<T,OUT> b2) {
		return new Binder<IN,OUT>() {public OUT bind(IN in){ return b2.bind(b1.bind(in));} };
	}
	
	public static <IN,T1,T2,OUT> Binder<IN,OUT> compose(final Binder<IN,T1> b1, final Binder<T1,T2> b2, final Binder<T2,OUT> b3) {
		return new Binder<IN,OUT>() {public OUT bind(IN in){ return b3.bind(b2.bind(b1.bind(in)));} };
	}
	
	public static <IN,T1,T2,T3, OUT> Binder<IN,OUT> compose(final Binder<IN,T1> b1, final Binder<T1,T2> b2, final Binder<T2,T3> b3, final Binder<T3,OUT> b4) {
		return new Binder<IN,OUT>() {public OUT bind(IN in){ return b4.bind(b3.bind(b2.bind(b1.bind(in))));} };
	}

}