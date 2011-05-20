/**
 * 
 */
package org.ligo.lab.dsl;

import static org.ligo.lab.dsl.Ligo.*;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;
import org.ligo.lab.binders.Binder;
import org.ligo.lab.dsl.DummyLigoImpl.MyBinder;
import org.ligo.lab.dsl.DummyLigoImpl.MyBinderFactory;
import org.ligo.lab.dsl.DummyLigoImpl.MyData;
import org.ligo.lab.dsl.DummyLigoImpl.MyDataReader;


/**
 * @author Fabio Simeoni
 *
 */
public class DSLUseCases {

	
	@Test
	public void raw() {
	
		//the tool offers binders that instantiate types from data. no DSL required
		//it may well stop here: fixed model, bound type = projected type, external parsing.
		//goodies are in the quality of binders and for this the tool has chosen Ligo's 
		//(@Project and associated binding services, DI injection, and SPI-level facilities)
		
		//------------------------- configuration code -----------------------------------------------
		
		Binder<MyData,SomeType> binder = new MyBinder<SomeType>(SomeType.class);
		
		
		//------------------------- binding code -----------------------------------------------------
		
		MyData data = new MyData();
		
		binder.bind(data);
		
		//minimality notwithstanding, there are a number of generic observations we can make already from
		//client perspective:
		
		// a) Binder is a type-specific abstraction (a single binder binds a given type multiple times). 
		// Compare with SNAQue or JAXB or ...Clients that bind multiple types use multiple binders 
		// (which does not exclude that they may have been configured to share state).
		
		// b) data parsing, if required, is dealt with upstream. the tool will offer a separate factory/ies
		// appropriate to MyData model.
		
		// c) data can be created programmatically and passed (or arrive somehow) to "binding code" without
		// the need of intermediate serialisation (as one would need to do with JAXB and XML). If
		// the binder is based on true projections, this enables a by-copy form of metamorphoses. Don't think JAXB
		// has ever been used to enable type changes, even when its binding logic would be appropriate.
		
		//d) clients use Ligo interfaces in "binding code", which allows configuration to grow later, independently.
		
		//e) DI container can easily create and inject dependencies into the binder, and then the binder in "binding code".
		//This is particularly of value when the binder has various dependencies to other components of the tools.
		
		//f) for increased decoupling from Ligo API, clients can get DI to create and inject a Provider<MyType> that takes
		//data and produces instances, using an (injected) Binder inside. Ligo dependencies would thus be isolated
		//in source (a la Guice modules), perhaps even in a separate build module (perhaps with overlapping packages for
		//package private access). Not sure how much value to attach to this extra effort in this simple scenario.
		
		
	}

	
	@Test
	public void parsing() {
	
		//DSL is introduced only for the convenience of encapsulating parsing within the binder.
		//the Ligo implementation offers a number of input-type specific parsers.
		
		//------------------------- configuration code -----------------------------------------------
		
		MyBinder<SomeType> binder = new MyBinder<SomeType>(SomeType.class);
		
		MyDataReader parser = new MyDataReader();
		
		Binder<Reader,SomeType> readingbinder = bind(SomeType.class).with(binder).and(parser);
		
		//------------------------- binding code -----------------------------------------------------
		
		Reader stream = new StringReader(".......");
	
		readingbinder.bind(stream);
		
		//a) types are usually bound to data that becomes available in the same form.
		//hence input-type parsers (e.g. for character streams) are common.
		
		//b) the encapsulation of sufficiently general parser (e.g. Readers indeed)
		//can hide the data model from "binding code". evolving it along with the rest 
		// of the binding configuration leaves the "binding code" untouched. not sure this
		//flexibility is actually required in practice.
		
		//c) DI cannot inject into sentence (especially not in Spring, not without a special parser anyway).
		//Clients that use DSL sentences inject Binders and Parsers into Provider<MyType> and Provider<MyType>s into
		//the other components.
	}
	
	
	@Test
	public void generatedbinders() {
	
		//DSL is used to introduce dynamic binding generation in the configuration process.
		//binders are generated from factories, typically because they operate by dynamical dispatch.
		
		
		//------------------------- configuration code -----------------------------------------------
		
		MyBinderFactory<SomeType> factory = new MyBinderFactory<SomeType>();
		
		MyDataReader parser = new MyDataReader();
		
		Binder<Reader,SomeType> binder = bind(SomeType.class).with(factory).and(parser);
		
		
		//------------------------- binding code -----------------------------------------------------
		
		Reader stream = new StringReader(".......");
	
		binder.bind(stream);
		
	}
	
}

//sample app model
class SomeType {}