/**
 * 
 */
package org.ligo.lab.dsl;

import java.io.Reader;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.DataBinder;
import org.ligo.lab.binders.DataBinderFactory;
import org.ligo.lab.binders.TypeBinderFactory;

/**
 * @author Fabio Simeoni
 *
 */
public class DummyLigoImpl {

	//sample impl
	static class MyData {}
	
	static class MyDataReader implements DataBinder<Reader,MyData> {
		public MyData bind(Reader in) {
			System.out.println("parsing with a "+in.getClass().getSimpleName());
			return new MyData();
		}
	}

	static class MyBinder<T> implements Binder<MyData,T> {
		private Class<T> type;
		public MyBinder(Class<T> t) {
			type=t;
		}
		public T bind(MyData in) {
			System.out.println("binding "+in.getClass().getSimpleName());
			try {
				return type.newInstance();
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	static class MyBinderFactory<T> implements TypeBinderFactory<MyData,T> {
		/**{@inheritDoc}*/
		@Override
		public MyBinder<T> bind(Class<T> in) {
			System.out.println("generating binder from "+in.getSimpleName());
			return new MyBinder<T>(in);
		}
	}

	static class MatchBinder<T> implements Binder<Match,T> {
		private Class<T> type;
		public MatchBinder(Class<T> t) {
			type=t;
		}
		public T bind(Match in) {
			System.out.println("binding "+in.getClass().getSimpleName());
			try {
				return type.newInstance();
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	static class Match {}

	static class MyPattern implements DataBinder<MyData,Match> {
		public Match bind(MyData in) {
			System.out.println("matching "+in);
			return new Match();
		}
	}

	static class MyPatternFactory<T> implements DataBinderFactory<T,MyData,Match> {
		
		/**{@inheritDoc}*/
		@Override
		public MyPattern bind(Class<T> in) {
			System.out.println("generating pattern from "+in.getSimpleName());
			return new MyPattern();
		}
	}

	static class MyDefaults {
		public static DSLDefaults<MyData> DEFAULTS = new DSLDefaults<MyData>() {
			@Override
			public <TYPE> EndClause<TYPE,MyData> complete(WithClause<TYPE> clause, ClauseContext<TYPE,?,?> ctxt) {
				return clause.with(new MyPatternFactory<TYPE>()).and(new MatchBinder<TYPE>(ctxt.type()));
			}
		};
		public static DSLDefaults<MyData> OTHERDEFAULTS = new DSLDefaults<MyData>() {
			@Override
			public <TYPE> EndClause<TYPE,MyData> complete(WithClause<TYPE> clause, ClauseContext<TYPE,?,?> ctxt) {
				return clause.with(new MyBinder<TYPE>(ctxt.type()));
			}
		};
		
		
	}
}
