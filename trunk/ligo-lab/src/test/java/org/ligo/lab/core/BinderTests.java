/**
 * 
 */
package org.ligo.lab.core;

import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.ligo.lab.core.TestData.*;
import static org.ligo.lab.core.annotations.Bind.Mode.*;
import static org.ligo.lab.core.kinds.Kind.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ligo.lab.core.TestClassDefs.Adapted;
import org.ligo.lab.core.TestClassDefs.Adapted2;
import org.ligo.lab.core.TestClassDefs.BadPlacement;
import org.ligo.lab.core.TestClassDefs.BindOnManyParams;
import org.ligo.lab.core.TestClassDefs.BindOnMethod;
import org.ligo.lab.core.TestClassDefs.BindOnParam;
import org.ligo.lab.core.TestClassDefs.DuplicateLabels;
import org.ligo.lab.core.TestClassDefs.Empty;
import org.ligo.lab.core.TestClassDefs.InterfaceImpl1;
import org.ligo.lab.core.TestClassDefs.InterfaceImpl2;
import org.ligo.lab.core.TestClassDefs.Lax1;
import org.ligo.lab.core.TestClassDefs.Lax2;
import org.ligo.lab.core.TestClassDefs.MultiParams;
import org.ligo.lab.core.TestClassDefs.Partial;
import org.ligo.lab.core.TestClassDefs.Primitive;
import org.ligo.lab.core.TestClassDefs.SomeInterface;
import org.ligo.lab.core.TestClassDefs.TooManyConstructors;
import org.ligo.lab.core.data.Provided;
import org.ligo.lab.core.impl.DefaultEnvironment;
import org.ligo.lab.core.keys.Key;
import org.ligo.lab.core.kinds.Kind;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author Fabio Simeoni
 *
 */
public class BinderTests {

	DefaultEnvironment env;
	
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
		
		env = new DefaultEnvironment(p);
	}

	@Test
	public void primitive() {
		
		TypeBinder<String> sb = env.binderFor(String.class);
		String bound = sb.bind(v("hello"));
		
		assertEquals("hello",bound);
		
		sb.bind(v(3));
		
		try {
			sb.bind(s(p("1",v("hello"))));
			fail();
		}
		catch(RuntimeException e) {
			System.out.println("caught expected:"+e.getMessage());
		}
		
		List<Provided> ps = new ArrayList<Provided>(v("hello"));
		ps.add(v("world").get(0));
		try {
			sb.bind(ps);
			fail();
		}
		catch(RuntimeException e) {
			System.out.println("caught expected:"+e.getMessage());
		}
		
		sb.setMode(LAX);
		
		bound = sb.bind(v(3));
		assertEquals("3",bound);
		
		bound = sb.bind(s(p("1",v("hello"))));
		assertNull(bound);
		
		bound = sb.bind(ps);
		assertNull(bound);
	}
	
	@Test
	public void otherprimitive() {
		
		TypeBinder<Integer> sb = env.binderFor(Integer.class);
		
		int bound = sb.bind(v(1));
		assertEquals(1,bound);
		
		bound = sb.bind(v("1"));
		assertEquals(1,bound);
		
		try {
			sb.bind(v("hello"));
			fail();
		}
		catch(RuntimeException e) {
			System.out.println("caught expected:"+e.getMessage());
		}
		
		sb.setMode(LAX);
		
		bound = sb.bind(v("hello"));
		assertEquals(0,bound);
		
		
	}
	
	@Test
	public void emptyObject() {
		
		TypeBinder<Empty> b = env.binderFor(Empty.class);
		
		try{
			b.bind(v(5));
			fail();
		}
		catch(Exception e) {
			System.out.println("caught expected:"+e.getMessage());
		}
		
		SomeInterface r = b.bind(s());
		assertNotNull(r);
	}

	@Test 
	public void badConstructors() throws Exception {
	
		//badly annotated constructor
		try {
			env.binderFor(BadPlacement.class);
			fail();
		}
		catch(Exception e) {
			System.out.println("caught expected:"+e.getMessage());
		}
		
		//ambiguous constructors
		try {
			env.binderFor(TooManyConstructors.class);
			fail();
		}
		catch(Exception e) {
			System.out.println("caught expected:"+e.getMessage());
		}
	}
	
	@Test 
	public void bindplacement() throws Exception {
		
		//on method
		TypeBinder<BindOnMethod> b1 = env.binderFor(BindOnMethod.class);
		
		BindOnMethod r1 = b1.bind(s(p("a","hello")));
		assertNotNull(r1);
		assertTrue(r1.invoked);
		
		//on param
		TypeBinder<BindOnParam> b2 = env.binderFor(BindOnParam.class);;
		
		BindOnParam r2 = b2.bind(s(p("a","hello")));
		assertNotNull(r2);
		assertTrue(r2.invoked);
		
		//on many param
		TypeBinder<BindOnManyParams> b3 = env.binderFor(BindOnManyParams.class);
		
		BindOnManyParams r3 = b3.bind(s(p("a","hello"),p("b","world")));
		assertNotNull(r3);
		assertTrue(r3.invoked);
		
	}

	@Test 
	public void primitiveField() throws Exception {
		
		TypeBinder<Primitive> b = env.binderFor(Primitive.class);;
		
		Primitive r = b.bind(s(p("1",0),p("2",0),p("3",0),p("4",0),p("5",0),p("6",0),p("7",'c'),p("8",true)));
		assertNotNull(r);
		assertEquals(8,r.invoked.size());
	}
	
	@Test 
	public void multiparams() throws Exception {
	
		TypeBinder<MultiParams> b = env.binderFor(MultiParams.class);
		
		MultiParams r = b.bind(s(p("a","hello"),p("b","world")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test 
	public void duplicateLabels() throws Exception {
	
		//ambiguous constructors
		try {
			env.binderFor(DuplicateLabels.class);
			fail();
		}
		catch(Exception e) {
			System.out.println("caught expected:"+e.getMessage());
		}
	}
	
	@Test 
	public void partialbinding() throws Exception {
	
		TypeBinder<Partial> b = env.binderFor(Partial.class);
		
		Partial r = b.bind(s(p("a","hello"),p("b","world")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test 
	public void lax1() throws Exception {
	
		TypeBinder<Lax1> b = env.binderFor(Lax1.class);
		
		//a is missing, b is wrongly typed
		Lax1 r = b.bind(s(p("b","world")));
		assertNotNull(r);
		assertTrue(r.invoked1);
		assertTrue(r.invoked2);
		
		//all is missing
		r = b.bind(s());
		assertNotNull(r);
		assertTrue(r.invoked1);
		assertTrue(r.invoked2);
	}
	
	@Test 
	public void lax2() throws Exception {
	
		TypeBinder<Lax2> b = env.binderFor(Lax2.class);
		
		//a is wrongly typed
		Lax2 r = b.bind(s(p("a",3)));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test 
	public void throughInterface1() throws Exception {
	
		TypeBinder<InterfaceImpl1> b = env.binderFor(InterfaceImpl1.class);
		
		//a is wrongly typed
		InterfaceImpl1 r = b.bind(s(p("a","hello")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test 
	public void throughInterface2() throws Exception {
	
		TypeBinder<InterfaceImpl2> b = env.binderFor(InterfaceImpl2.class);
		
		//a is wrongly typed
		InterfaceImpl2 r = b.bind(s(p("b","hello")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	
	@Test 
	public void adapted() throws Exception {
	
		TypeBinder<Adapted> b = env.binderFor(Adapted.class);
		
		//a is wrongly typed
		Adapted r = b.bind(s(p("a","hello")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test 
	public void adapted2() throws Exception {
	
		TypeBinder<Adapted2> b = env.binderFor(Adapted2.class);
		
		//a is wrongly typed
		Adapted2 r = b.bind(s(p("person",s(p("name","John"),p("age",20)))));
		assertNotNull(r);
		assertTrue(r.invoked);
	}


	
	public static void main(String[] args) {
		
		System.out.println(s(p("person",s(p("name","John"),p("age",20)))));
	}
	
	
	


}

