/**
 * 
 */
package org.ligo.lab.dsl;

import java.io.Reader;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.BinderFactory;

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
		private Class<T> type;
		public DataBinder(Class<T> t) {
			type=t;
		}
		public T bind(Data in) {
			System.out.println("binding "+in.getClass().getSimpleName());
			try {
				return type.newInstance();
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	static class DataBinderFactory<T> implements BinderFactory<Class<T>,Data,T> {
		/**{@inheritDoc}*/
		@Override
		public DataBinder<T> bind(Class<T> in) {
			System.out.println("generating binder from "+in.getSimpleName());
			return new DataBinder<T>(in);
		}
	}
	
	
	static class Transform implements Binder<Data,Data> {
		public Data bind(Data in) {
			System.out.println("filtering/pruning "+in);
			return new Data();
		}
	}
	
	static class TransformFactory<T> implements BinderFactory<Class<T>,Data,Data> {
		
		/**{@inheritDoc}*/
		@Override
		public Transform bind(Class<T> in) {
			System.out.println("generating closed transform from "+in.getSimpleName());
			return new Transform();
		}
	}
	
	
	
	
	

	static class TransformedData {}

	static class TransformedDataBinder<T> implements Binder<TransformedData,T> {
		private Class<T> type;
		public TransformedDataBinder(Class<T> t) {
			type=t;
		}
		public T bind(TransformedData in) {
			System.out.println("binding "+in.getClass().getSimpleName());
			try {
				return type.newInstance();
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	static class TransformedDataBinderFactory<T> implements BinderFactory<Class<T>,TransformedData,T> {
		/**{@inheritDoc}*/
		@Override
		public TransformedDataBinder<T> bind(Class<T> in) {
			System.out.println("generating binder for transformed data from "+in.getSimpleName());
			return new TransformedDataBinder<T>(in);
		}
	}
	
	static class OpenTransform implements Binder<Data,TransformedData> {
		public TransformedData bind(Data in) {
			System.out.println("transforming "+in+" to another model");
			return new TransformedData();
		}
	}

	static class OpenTransformFactory<T> implements BinderFactory<Class<T>,Data,TransformedData> {
		
		/**{@inheritDoc}*/
		@Override
		public OpenTransform bind(Class<T> in) {
			System.out.println("generating open transform from "+in.getSimpleName());
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
				return clause.with(new DataBinder<TYPE>(ctxt.type()));
			}
		};
		
		
	}
}
