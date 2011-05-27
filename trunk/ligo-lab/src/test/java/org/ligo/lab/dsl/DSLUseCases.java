/**
 * 
 */
package org.ligo.lab.dsl;

import static org.ligo.lab.dsl.DummyLigoImpl.Defaults.*;
import static org.ligo.lab.dsl.Ligo.*;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;
import org.ligo.lab.binders.Binder;
import org.ligo.lab.core.Literal;
import org.ligo.lab.dsl.DummyLigoImpl.Data;
import org.ligo.lab.dsl.DummyLigoImpl.DataBinder;
import org.ligo.lab.dsl.DummyLigoImpl.DataBinderFactory;
import org.ligo.lab.dsl.DummyLigoImpl.DataReader;
import org.ligo.lab.dsl.DummyLigoImpl.OpenTransform;
import org.ligo.lab.dsl.DummyLigoImpl.OpenTransformFactory;
import org.ligo.lab.dsl.DummyLigoImpl.Transform;
import org.ligo.lab.dsl.DummyLigoImpl.TransformFactory;
import org.ligo.lab.dsl.DummyLigoImpl.TransformedData;
import org.ligo.lab.dsl.DummyLigoImpl.TransformedDataBinder;
import org.ligo.lab.dsl.DummyLigoImpl.TransformedDataBinderFactory;


/**
 * @author Fabio Simeoni
 *
 */
public class DSLUseCases {

	
	@Test
	public void raw() {
	
		//here the binding tool offers binders that instantiate types from data.
		//no data pipeling is required, hence no DSL: fixed model, out-of-band parsing facilities, bound type = projected type.
		//tools uses Ligo used for its SPI facilities (@Bind and associated binding services, DI injection)
		
		//------------------------- configuration code -----------------------------------------------
		
		Binder<Data,SomeType> binder = new DataBinder<SomeType>(SomeType.class);
		
		
		//------------------------- binding code -----------------------------------------------------
		
		Data data = new Data();
		
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
		// appropriate to Data model.
		
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
		
		DataBinder<SomeType> binder = new DataBinder<SomeType>(SomeType.class);
		
		DataReader parser = new DataReader();
		
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
	public void transformPipe() {
	
		//here we add to pipeline to transform the data upstream of binding.
		
		//------------------------- configuration code -----------------------------------------------
		
		DataBinder<SomeType> binder = new DataBinder<SomeType>(SomeType.class);
		
		Transform transform = new Transform();
		
		Binder<Data,SomeType> transformingbinder = bind(SomeType.class).with(binder).and(transform).build();
		
		//------------------------- binding code -----------------------------------------------------
		
		Data data = new Data();
		
		transformingbinder.bind(data);
		
		//of course, parsing can be chained. let's show this once for all:
		
		
		//------------------------- configuration code -----------------------------------------------
		
		DataReader parser = new DataReader();
		
		Binder<Reader,SomeType> databinder = bind(SomeType.class).with(binder).and(transform).and(parser).build();
		
		//------------------------- binding code -----------------------------------------------
	
		Reader stream = new StringReader(".......");
	
		databinder.bind(stream);
	}
	
	
	@Test
	public void opentransformPipe() {
	
		//pipeline steps do not need to be 'closed' under a single data model:
		//for example a tool can offer transforms from an input model to an output model which is then bound.
		
		
		//------------------------- configuration code -----------------------------------------------
		
		TransformedDataBinder<SomeType> binder = new TransformedDataBinder<SomeType>(SomeType.class);
		
		
		OpenTransform transform = new OpenTransform();
		
		//open transform change data model
		assert( transform.bind(new Data()) instanceof TransformedData);
		

		Binder<Data,SomeType> transformingbinder = bind(SomeType.class).with(binder).and(transform).build();
		
		//------------------------- binding code -----------------------------------------------------
		
		Data data = new Data();
		
		transformingbinder.bind(data);
	}
	
	@Test
	public void bindingFactories() {
	
		//tools will normally generate binders from factories, and these factories can be lifted in the configuration instead 
		//of hiding without a binder front-end.
		
		//------------------------- configuration code -----------------------------------------------
		
		DataBinderFactory<SomeType> factory = new DataBinderFactory<SomeType>();
		
		Binder<Data,SomeType> binder = bind(SomeType.class).with(factory).build();
		
		
		//------------------------- binding code -----------------------------------------------------
		
		Data data = new Data();
	
		binder.bind(data);;
	}
	
	@Test
	public void transformFactories() {
	
		//similarly, transformations be derived by type-driven factories.
		//here we show a combination of binder and transformation factories:
		
		//------------------------- configuration code -----------------------------------------------
		
		DataBinderFactory<SomeType> bfactory = new DataBinderFactory<SomeType>();
		
		TransformFactory<SomeType> tfactory = new TransformFactory<SomeType>();
		
		Binder<Data,SomeType> binder = bind(SomeType.class).with(bfactory).and(tfactory).build();
		
		
		//------------------------- binding code -----------------------------------------------------
		
		Data data = new Data();
	
		binder.bind(data);
	}
	
	@Test
	public void defaults() {
	
		//tools may offer a single pipeline, or at least a default one.
		//when this becomes complex, the tool may provide defaults that can be 'completed' in configuration code
		
		//e.g. consider this (artificially?) heavy pipline with factories, heterogeneous transforms, and parsers:
		
		
		
		//------------------------- configuration code -----------------------------------------------
		
		
		DataReader parser = new DataReader();
		TransformFactory<SomeType> tfactory = new TransformFactory<SomeType>();
		OpenTransformFactory<SomeType> ttfactory = new OpenTransformFactory<SomeType>();
		TransformedDataBinderFactory<SomeType> tbfactory = new TransformedDataBinderFactory<SomeType>();
				
		Binder<Reader,SomeType> binder = bind(SomeType.class).with(tbfactory).and(ttfactory).and(tfactory).and(parser).build();
		
		//part of the construction can be instead captured by default provided by the tool, and the completed as required:
		
		binder = bind(SomeType.class).with(FULLPIPE).and(parser).build();
		
		//------------------------- binding code -----------------------------------------------------
		Reader stream = new StringReader(".......");
		
		binder.bind(stream);
	}
	
	@Test
	public void generics() {
		
		Literal<SomeGeneric<SomeType>> literal = new Literal<SomeGeneric<SomeType>>() {};
		DataBinder<SomeGeneric<SomeType>> binder = new DataBinder<SomeGeneric<SomeType>>(literal);
		
		DataReader parser = new DataReader();
		
		Binder<Reader,SomeGeneric<SomeType>> readingbinder = bind(literal).with(binder).and(parser).build();
		
		//------------------------- binding code -----------------------------------------------------
		
		Reader stream = new StringReader(".......");
	
		readingbinder.bind(stream);
	}
}


class SomeGeneric<T> {}
//sample app model
class SomeType {}