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
	
		//here the binding tool offers binders that instantiate types from data.
		//no data pipeling is required, hence no DSL: fixed model, out-of-band parsing facilities, bound type = projected type.
		//tools uses Ligo used for its SPI facilities (@Project and associated binding services, DI injection)
		
		//------------------------- configuration code -----------------------------------------------
		
		Binder<MyData,SomeType> binder = new MyBinder<SomeType>(SomeType.class);
		
		
		//------------------------- binding code -----------------------------------------------------
		
		MyData data = new MyData();
		
		binder.bind(data);
		
		//minimality notwithstanding, there are a number of generic observations we can make already from
		//client perspective:
		
		// -) In contrast with JAXB, SNAQue, and other tools, binder is a type-specific abstraction 
		//(a single binder binds a given type one or more times). 
		// scheme does not limit implementation choices. binder may pre-process MyType,
		//use a cache of pre-processed representations, recur with dynamic dispatch over the representations, etc.
		//Clients that bind multiple types use multiple binders (which does not exclude 
		//that they may have been configured to share state).
		
		// -) data parsing, if required, is dealt with upstream. the tool will offer a separate factory/ies
		// appropriate to MyData model.
		
		// -) data can be created programmatically and passed (or arrive somehow) to "binding code" without
		// the need of intermediate serialisation (as one would need to do with JAXB and XML). If
		// the binder is based on true projections, this enables a by-copy form of metamorphoses. Don't think JAXB
		// has ever been used to enable type changes, even when its binding logic would be appropriate.
		
		//-) clients use Ligo interfaces in "binding code", which allows configuration to grow later, independently.
		
		//-) DI container can easily create and inject dependencies into the binder, and then the binder in "binding code".
		//This is particularly of value when the binder has various dependencies to other components of the tools.
		
		//-) for increased decoupling from Ligo API, clients can get DI to create and inject a Provider<MyType> that takes
		//data and produces instances, using an (injected) Binder inside. Ligo dependencies would thus be isolated
		//in source (a la Guice modules), perhaps even in a separate build module (perhaps with overlapping packages for
		//package private access). Not sure how much value to attach to this extra effort in this simple scenario.
		
		
	}

	
	@Test
	public void parsingPipe() {
	
		//here a simple pipeline is introduced which encapsulates parsing within the binder.
		//the binding tool offers a number of input-type specific parsers.
		
		//------------------------- configuration code -----------------------------------------------
		
		MyBinder<SomeType> binder = new MyBinder<SomeType>(SomeType.class);
		
		MyDataReader parser = new MyDataReader();
		
		Binder<Reader,SomeType> readingbinder = bind(SomeType.class).with(binder).and(parser).build();
		
		//------------------------- binding code -----------------------------------------------------
		
		Reader stream = new StringReader(".......");
	
		readingbinder.bind(stream);
		
		//-) types are usually bound to data that becomes available in the same form.
		//hence input-type parsers (e.g. for character streams) are common.
		
		//-) the encapsulation of sufficiently general parser (e.g. Readers indeed)
		//can hide the data model from "binding code". evolving it along with the rest 
		// of the binding configuration leaves the "binding code" untouched. not sure this
		//flexibility is actually required in practice.
		
		//-) DI cannot inject into sentence (especially not in Spring, not without a special parser anyway).
		//Clients that use DSL sentences inject Binders and Parsers into Provider<MyType> and Provider<MyType>s into
		//the other components.
	}
	
	
	@Test
	public void generatedbinders() {
	
		//DSL is used to insert dynamic binding generation in the configuration process,
		//instead of hiding without a binder front-end.
		
		//------------------------- configuration code -----------------------------------------------
		
		MyBinderFactory<SomeType> factory = new MyBinderFactory<SomeType>();
		
		MyDataReader parser = new MyDataReader();
		
		Binder<Reader,SomeType> binder = bind(SomeType.class).with(factory).and(parser).build();
		
		
		//------------------------- binding code -----------------------------------------------------
		
		Reader stream = new StringReader(".......");
	
		binder.bind(stream);
		
		//if cross-input parsing is desirable over model abstraction:
		
		Binder<MyData,SomeType> databinder = bind(SomeType.class).with(factory).build();
		
		databinder.bind(new MyData());
	}
	
	@Test
	public void newtypes() {
	
		//DSL is here used to separate the type to be bound from the type to be used for projection.
		//the tool offers binders from the data model to the same or another model. the output model
		//is then bound to instances of the initial type.
		
		//------------------------- configuration code -----------------------------------------------
		
		MyBinderFactory<SomeType> factory = new MyBinderFactory<SomeType>();
		
		MyDataReader parser = new MyDataReader();
		
		Binder<Reader,SomeType> binder = bind(SomeType.class).with(factory).and(parser).build();
		
		
		//------------------------- binding code -----------------------------------------------------
		
		Reader stream = new StringReader(".......");
	
		binder.bind(stream);
		
		//if cross-input parsing is desirable over model abstraction:
		
		Binder<MyData,SomeType> databinder = bind(SomeType.class).with(factory).build();
		
		databinder.bind(new MyData());
	}
}

//sample app model
class SomeType {}