/**
 * 
 */
package org.ligo.dsl;

import static org.ligo.core.keys.Keys.*;

import java.io.Reader;

import org.ligo.core.binders.api.Binder;
import org.ligo.core.binders.api.BinderFactory;
import org.ligo.core.keys.Key;
import org.ligo.core.keys.Literal;
import org.ligo.dsl.AndClause;
import org.ligo.dsl.ClauseContext;
import org.ligo.dsl.DSLDefaults;
import org.ligo.dsl.WithClause;

/**
 * @author Fabio Simeoni
 *
 */
public class DummyLigoImpl {

	//sample impl
	static class Data {}
	
	static class DataReader implements Binder<Reader,Data> {
		public Data bind(Reader in) {
			System.out.println("parsing with a "+in.getClass().getSimpleName());
			return new Data();
		}
	}

	static class DataBinder<T> implements Binder<Data,T> {
		private Key<T> key;
		public DataBinder(Key<T> t) {
			key=t;
		}
		public DataBinder(Literal<T> t) {
			key=newKey(t);
		}
		public DataBinder(Class<T> t) {
			key=newKey(t);
		}
		public T bind(Data in) {
			System.out.println("binding "+in.getClass().getSimpleName()+" to "+key.kind());
			return null;
		}
	}

	static class DataBinderFactory<T> implements BinderFactory<T,Data,T> {
		/**{@inheritDoc}*/
		@Override
		public DataBinder<T> binderFor(Key<T> in) {
			System.out.println("generating binder from "+in.kind());
			return new DataBinder<T>(in);
		}
	}
	
	
	static class Transform implements Binder<Data,Data> {
		public Data bind(Data in) {
			System.out.println("filtering/pruning "+in);
			return new Data();
		}
	}
	
	static class TransformFactory<T> implements BinderFactory<T,Data,Data> {
		
		/**{@inheritDoc}*/
		@Override
		public Transform binderFor(Key<T> in) {
			System.out.println("generating closed transform from "+in.kind());
			return new Transform();
		}
	}
	
	
	
	
	

	static class TransformedData {}

	static class TransformedDataBinder<T> implements Binder<TransformedData,T> {
		private Key<T> key;
		public TransformedDataBinder(Key<T> t) {
			key=t;
		}
		public TransformedDataBinder(Class<T> t) {
			key=newKey(t);
		}
		public T bind(TransformedData in) {
			System.out.println("binding "+in.getClass().getSimpleName()+" to "+key.kind());
			return null;
		}
	}
	
	static class TransformedDataBinderFactory<T> implements BinderFactory<T,TransformedData,T> {
		/**{@inheritDoc}*/
		@Override
		public TransformedDataBinder<T> binderFor(Key<T> in) {
			System.out.println("generating binder for transformed data from "+in.kind());
			return new TransformedDataBinder<T>(in);
		}
	}
	
	static class OpenTransform implements Binder<Data,TransformedData> {
		public TransformedData bind(Data in) {
			System.out.println("transforming "+in+" to another model");
			return new TransformedData();
		}
	}

	static class OpenTransformFactory<T> implements BinderFactory<T,Data,TransformedData> {
		
		/**{@inheritDoc}*/
		@Override
		public OpenTransform binderFor(Key<T> in) {
			System.out.println("generating open transform from "+in.kind());
			return new OpenTransform();
		}
	}


	static class Defaults {
		public static DSLDefaults<Data> FULLPIPE = new DSLDefaults<Data>() {
			@Override
			public <TYPE> AndClause<TYPE,Data> complete(WithClause<TYPE> clause, ClauseContext<TYPE,?> ctxt) {
				
				return clause.with(new TransformedDataBinderFactory<TYPE>()).and(new OpenTransformFactory<TYPE>()).and(new TransformFactory<TYPE>());
			}
		};
		public static DSLDefaults<Data> OTHERDEFAULTS = new DSLDefaults<Data>() {
			@Override
			public <TYPE> AndClause<TYPE,Data> complete(WithClause<TYPE> clause, ClauseContext<TYPE,?> ctxt) {
				return clause.with(new DataBinder<TYPE>(ctxt.key()));
			}
		};
		
		
	}
}
