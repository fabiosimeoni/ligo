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
import org.ligo.lab.core.TestGenericsClassDefs.MyAnnotatedGeneric;
import org.ligo.lab.core.TestGenericsClassDefs.MyGeneric;
import org.ligo.lab.core.TestGenericsClassDefs.NestedGeneric;
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
	
		TypeBinder<Generic<String>> b = factory.binderFor(new Literal<Generic<String>>() {});
		
		Generic<String> r = b.bind(s(p("b","hello")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test
	public void nestedGeneric() {
	
		TypeBinder<NestedGeneric<String>> b = factory.binderFor(new Literal<NestedGeneric<String>>() {});
		
		NestedGeneric<String> r = b.bind(s(p("b",s(p("b","hello"))),p("c","world")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test
	public void genericField() {
		
		TypeBinder<GenericField> b = factory.binderFor(GenericField.class);	
		
		GenericField r = b.bind(s(
								p("a",s(p("b","hello"))),
								p("b",s(p("b",5))),
								p("c",s(p("b",s(p("a",
										s(p("b",5)))))))));
		assertNotNull(r);
		assertTrue(r.invokedm);
		assertTrue(r.invokedn);
	}
	
	@Test
	public void subclass() {
		
		TypeBinder<MyGeneric> b = factory.binderFor(MyGeneric.class);	
		
		MyGeneric r = b.bind(s(p("b","hello"),p("c",3)));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test
	public void annotatedSubclass() {
		
		TypeBinder<MyAnnotatedGeneric> b = factory.binderFor(MyAnnotatedGeneric.class);	
		
		MyAnnotatedGeneric r = b.bind(s(p("a","hello"),p("c",3)));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
}

