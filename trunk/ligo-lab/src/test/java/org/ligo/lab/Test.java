/**
 * 
 */
package org.ligo.lab;

import static org.ligo.lab.dsl.Ligo.*;

import java.io.Reader;
import java.io.StringReader;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.BinderFactory;
import org.ligo.lab.binders.DataBinder;
import org.ligo.lab.binders.DataBinderFactory;


/**
 * @author Fabio Simeoni
 *
 */
public class Test {

	public static void main(String[] args) {
		
		System.out.println("---- use case");

		//config
		MyBinder binder = new MyBinder();
		
		//define
		Binder<Data,MyType> simple = bind(MyType.class).with(binder).build();

		//use 
		Data data = new Data();
		MyType bound = simple.bind(data);

		
		
		
		System.out.println("---- use case");

		//config
		MyDataReader parser = new MyDataReader();
		
		//define
		Binder<Reader,MyType> readersimple = bind(MyType.class).with(binder).and(parser);
		
		//use
		Reader stream = new StringReader("sample stream");
		bound = readersimple.bind(stream);
		

		System.out.println("---- use case");

		//config
		MyBinderFactory bfactory = new MyBinderFactory();
		
		//define
		Binder<Reader,MyType> derived = bind(MyType.class).with(bfactory).and(parser);
		
		//use
		bound = derived.bind(stream);
		
		
		System.out.println("---- use case");
		
		//config
		MyPattern pattern = new MyPattern();
		MatchBinder mbinder = new MatchBinder();
		
		//define
		Binder<Data,MyType> patterned = bind(MyType.class).with(pattern).and(mbinder).build();
		
		//use
		bound = patterned.bind(data);
		
		
		System.out.println("---- use case");
		
		//define
		Binder<Reader,MyType> readerPatterned = bind(MyType.class).with(pattern).and(mbinder).and(parser);

		//user
		bound = readerPatterned.bind(stream);
		
		
		
		System.out.println("---- use case");
		
		//config
		MyPatternFactory<MyType> factory = new MyPatternFactory<MyType>();
		
		//define
		Binder<Data,MyType> generated = bind(MyType.class).with(factory).and(mbinder).build();
		
		//use
		bound = generated.bind(data);
		
		
		System.out.println("---- use case");
		
		//define
		Binder<Reader,MyType> readerTranslated = bind(MyType.class).with(factory).and(mbinder).and(parser);
		
		//use
		bound = readerTranslated.bind(stream);
		
		System.out.println(bound);
	}
}

//sample impl
class Data {}
class MyDataReader implements DataBinder<Reader,Data> {
	public Data bind(Reader in) {
		return new Data();
	}
}

class MyBinder implements Binder<Data,MyType> {
	public MyType bind(Data in) {
		System.out.println("binding "+in.getClass().getSimpleName());
		return new MyType();
	}
}

class MyBinderFactory implements BinderFactory<Data,MyType> {
	/**{@inheritDoc}*/
	@Override
	public MyBinder bind(Class<MyType> in) {
		System.out.println("generating binder from "+in.getSimpleName());
		return new MyBinder();
	}
}

class MatchBinder implements Binder<Match,MyType> {
	public MyType bind(Match in) {
		System.out.println("binding "+in.getClass().getSimpleName());
		return new MyType();
	}
}

class Match {}

class MyPattern implements DataBinder<Data,Match> {
	public Match bind(Data in) {
		System.out.println("matching "+in);
		return new Match();
	}
}

class MyPatternFactory<T> implements DataBinderFactory<T,Data,Match> {
	
	/**{@inheritDoc}*/
	@Override
	public MyPattern bind(Class<T> in) {
		System.out.println("generating pattern from "+in.getSimpleName());
		return new MyPattern();
	}
}

//sample app model
class MyType {}
