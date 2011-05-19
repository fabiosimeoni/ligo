/**
 * 
 */
package org.ligo.lab;

import static org.ligo.lab.MyDefaults.*;
import static org.ligo.lab.dsl.Ligo.*;

import java.io.Reader;
import java.io.StringReader;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.BinderFactory;
import org.ligo.lab.binders.DataBinder;
import org.ligo.lab.binders.DataBinderFactory;
import org.ligo.lab.binders.TypeBinder;
import org.ligo.lab.dsl.ClauseContext;
import org.ligo.lab.dsl.DSLDefaults;
import org.ligo.lab.dsl.EndClause;
import org.ligo.lab.dsl.WithClause;


/**
 * @author Fabio Simeoni
 *
 */
public class Test {

	public static void main(String[] args) {
		
		System.out.println("---- use case");

		//config
		MyBinder<SomeType> binder = new MyBinder<SomeType>(SomeType.class);
		
		//define
		Binder<MyData,SomeType> simple = bind(SomeType.class).with(binder).build();

		//use 
		MyData data = new MyData();
		SomeType bound = simple.bind(data);

		
		
		
		System.out.println("---- use case");

		//config
		MyDataReader parser = new MyDataReader();
		
		//define
		Binder<Reader,SomeType> readersimple = bind(SomeType.class).with(binder).and(parser);
		
		//use
		Reader stream = new StringReader("sample stream");
		bound = readersimple.bind(stream);
		

		System.out.println("---- use case");

		//config
		MyBinderFactory<SomeType> bfactory = new MyBinderFactory<SomeType>();
		
		//define
		Binder<Reader,SomeType> derived = bind(SomeType.class).with(bfactory).and(parser);
		
		//use
		bound = derived.bind(stream);
		
		
		System.out.println("---- use case");
		
		//config
		MyPattern pattern = new MyPattern();
		MatchBinder<SomeType> mbinder = new MatchBinder<SomeType>(SomeType.class);
		
		//define
		Binder<MyData,SomeType> patterned = bind(SomeType.class).with(pattern).and(mbinder).build();
		
		//use
		bound = patterned.bind(data);
		
		
		System.out.println("---- use case");
		
		//define
		Binder<Reader,SomeType> readerPatterned = bind(SomeType.class).with(pattern).and(mbinder).and(parser);

		//user
		bound = readerPatterned.bind(stream);
		
		
		
		System.out.println("---- use case");
		
		//config
		MyPatternFactory<SomeType> factory = new MyPatternFactory<SomeType>();
		
		//define
		Binder<MyData,SomeType> generated = bind(SomeType.class).with(factory).and(mbinder).build();
		
		//use
		bound = generated.bind(data);
		
		
		System.out.println("---- use case");
		
		//define
		Binder<Reader,SomeType> readerTranslated = bind(SomeType.class).with(factory).and(mbinder).and(parser);
		
		//use
		bound = readerTranslated.bind(stream);
		
		System.out.println(bound);
		
		
		System.out.println("---- use case");
		
		//define
		Binder<Reader,SomeType> defaulted = bind(SomeType.class).with(DEFAULTS).and(parser);
		
		//use
		bound = defaulted.bind(stream);
		
		System.out.println(bound);
	}
}

//sample impl
class MyData {}
class MyDataReader implements DataBinder<Reader,MyData> {
	public MyData bind(Reader in) {
		return new MyData();
	}
}

class MyBinder<T> implements TypeBinder<MyData,T> {
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

class MyBinderFactory<T> implements BinderFactory<MyData,T> {
	/**{@inheritDoc}*/
	@Override
	public MyBinder<T> bind(Class<T> in) {
		System.out.println("generating binder from "+in.getSimpleName());
		return new MyBinder<T>(in);
	}
}

class MatchBinder<T> implements Binder<Match,T> {
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

class Match {}

class MyPattern implements DataBinder<MyData,Match> {
	public Match bind(MyData in) {
		System.out.println("matching "+in);
		return new Match();
	}
}

class MyPatternFactory<T> implements DataBinderFactory<T,MyData,Match> {
	
	/**{@inheritDoc}*/
	@Override
	public MyPattern bind(Class<T> in) {
		System.out.println("generating pattern from "+in.getSimpleName());
		return new MyPattern();
	}
}

class MyDefaults implements DSLDefaults<MyData> {
	public static MyDefaults DEFAULTS = new MyDefaults();
	private MyDefaults(){};
	@Override
	public <TYPE> EndClause<TYPE,MyData> complete(WithClause<TYPE> clause, ClauseContext<TYPE,?,?> ctxt) {
		return clause.with(new MyPatternFactory<TYPE>()).and(new MatchBinder<TYPE>(ctxt.type()));
	}
}

//sample app model
class SomeType {}
