/**
 * 
 */
package org.ligo.lab.core;

import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.ligo.lab.core.TestData.*;
import static org.ligo.lab.core.kinds.Kind.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ligo.lab.core.TestGenericsClassDefs.Generic;
import org.ligo.lab.core.TestGenericsClassDefs.GenericField;
import org.ligo.lab.core.TestGenericsClassDefs.IndirectNestedGeneric;
import org.ligo.lab.core.TestGenericsClassDefs.MyAnnotatedGeneric;
import org.ligo.lab.core.TestGenericsClassDefs.MyGeneric;
import org.ligo.lab.core.TestGenericsClassDefs.NestedGeneric;
import org.ligo.lab.core.TestGenericsClassDefs.Sub;
import org.ligo.lab.core.impl.DefaultEnvironment;
import org.ligo.lab.core.keys.Key;
import org.ligo.lab.core.kinds.Kind;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author Fabio Simeoni
 *
 */
public class BinderGenericsTests {

	DefaultEnvironment factory;
	
	@Before 
	@SuppressWarnings("unchecked")
	public void setup() {
		
		Resolver p = mock(Resolver.class);
		when(p.resolve(any(Key.class))).thenAnswer(new Answer<List<Class<?>>>() {
			public List<Class<?>> answer(InvocationOnMock invocation) throws Throwable {
				return (List)singletonList(((Key)invocation.getArguments()[0]).kind().toClass());
			}
		});
		when(p.resolve(any(Key.class),any(List.class))).thenAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Kind<?> kind = ((Key)invocation.getArguments()[0]).kind();
				Object instance = null;
				try {
					switch(kind.value()) {
						case CLASS: instance = CLASS(kind).newInstance();break;
						case GENERIC: instance = ((Class<?>) GENERIC(kind).getRawType()).newInstance();
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				return instance;
			}
		});
		
		factory = new DefaultEnvironment(p);
	}

	@Test
	public void generic() {
	
		//standard, flat-generic
		TypeBinder<Generic<String>> b = factory.binderFor(new Literal<Generic<String>>() {});
		
		Generic<String> r = b.bind(s(p("b","hello")));
		
		assertNotNull(r);
		assertNotNull(r.t);
	}
	
	@Test
	public void genericOfgeneric() {
	
		//generic-of-a-generic: variable bindings retract along call chain
		TypeBinder<Generic<Generic<String>>> b = factory.binderFor(new Literal<Generic<Generic<String>>>() {});
		
		Generic<Generic<String>> r = b.bind(s(
												p("b",
													s(p("b","hello")))));
		assertNotNull(r);
		assertNotNull(r.t);
		assertNotNull(r.t.t);
	}
	
	@Test
	public void genericField() {
		
		//fields with different instantiations of same generic: Generic<String>, Generic<Integer>
		TypeBinder<GenericField> b = factory.binderFor(GenericField.class);	
		
		GenericField r = b.bind(s(
								p("a",s(p("b","hello"))),
								p("b",s(p("b",5)))));
		assertNotNull(r);
		assertNotNull(r.gs);
		assertNotNull(r.gi);

	}
	
	@Test
	public void nestedGeneric() {
	
		//generic inherits binding from outer one: NestedGeneric<T> contains Generic<T>
		TypeBinder<NestedGeneric<String>> b = factory.binderFor(new Literal<NestedGeneric<String>>() {});
		
		NestedGeneric<String> r = b.bind(s(p("b",s(p("b","hello"))),p("c","world")));
		
		assertNotNull(r);
		assertNotNull(r.t);
		assertNotNull(r.gt);
		assertNotNull(r.gt.t);
	}
	
	@Test
	public void indirectNestedGeneric() {
	
		//Generic<Dep> where Dep contains Generic<Integer>
		TypeBinder<IndirectNestedGeneric> b = factory.binderFor(new Literal<IndirectNestedGeneric>() {});
		
		IndirectNestedGeneric r = b.bind(s(
											p("a",
												s(p("b",
														s(p("a",
															s(p("b",5)))))))));
		
		assertNotNull(r);
		assertNotNull(r.gd);
		assertNotNull(r.gd.t);
		assertNotNull(r.gd.t.gi);
	}
	
	
	
	@Test
	public void subclass() {
		
		//Non generic class obtained by instantiation of generic one: MyGenerics extends Generic<String>
		// variable bindings go up the inheritance hierarchy so that we can process inherited methods 
		TypeBinder<MyGeneric> b = factory.binderFor(MyGeneric.class);	
		
		MyGeneric r = b.bind(s(p("b","hello"),p("c",3)));
		
		assertNotNull(r);
		assertNotNull(r.t);
		assertNotNull(r.i);
	}
	
	@Test
	public void annotatedSubclass() {
		
		//As before, but this this with overriding
		TypeBinder<MyAnnotatedGeneric> b = factory.binderFor(MyAnnotatedGeneric.class);	
		
		MyAnnotatedGeneric r = b.bind(s(p("a","hello"),p("c",3)));
		
		assertNotNull(r);
		assertNotNull(r.t);
		assertNotNull(r.i);
	}
	
	@Test
	public void inheritance() {
		
		//As before, but this this with overriding
		TypeBinder<Sub<Integer>> b = factory.binderFor(new Literal<Sub<Integer>>() {});	
		
		Sub<Integer> r = b.bind(s(p("a","hello"),p("b",3)));
		
		assertNotNull(r);
	}
}

