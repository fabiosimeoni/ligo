/**
 * 
 */
package org.ligo.lab.dsl.typebinders;

import static org.junit.Assert.*;
import static org.ligo.lab.dsl.typebinders.TestData.*;
import static org.ligo.lab.typebinders.Bind.Mode.*;
import static org.ligo.lab.typebinders.Key.*;
import static org.ligo.lab.typebinders.kinds.Kind.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ligo.lab.data.Provided;
import org.ligo.lab.dsl.typebinders.TestClassDefs.BadPlacement;
import org.ligo.lab.dsl.typebinders.TestClassDefs.BindOnManyParams;
import org.ligo.lab.dsl.typebinders.TestClassDefs.BindOnMethod;
import org.ligo.lab.dsl.typebinders.TestClassDefs.BindOnParam;
import org.ligo.lab.dsl.typebinders.TestClassDefs.DuplicateLabels;
import org.ligo.lab.dsl.typebinders.TestClassDefs.Empty;
import org.ligo.lab.dsl.typebinders.TestClassDefs.InterfaceImpl1;
import org.ligo.lab.dsl.typebinders.TestClassDefs.InterfaceImpl2;
import org.ligo.lab.dsl.typebinders.TestClassDefs.Lax1;
import org.ligo.lab.dsl.typebinders.TestClassDefs.Lax2;
import org.ligo.lab.dsl.typebinders.TestClassDefs.MultiParams;
import org.ligo.lab.dsl.typebinders.TestClassDefs.Partial;
import org.ligo.lab.dsl.typebinders.TestClassDefs.Primitive;
import org.ligo.lab.dsl.typebinders.TestClassDefs.SomeInterface;
import org.ligo.lab.dsl.typebinders.TestClassDefs.TooManyConstructors;
import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.Resolver;
import org.ligo.lab.typebinders.TypeBinder;
import org.ligo.lab.typebinders.impl.DefaultEnvironment;
import org.ligo.lab.typebinders.impl.DefaultObjectBinder;
import org.ligo.lab.typebinders.impl.PrimitiveBinder;
import org.ligo.lab.typebinders.kinds.Kind;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author Fabio Simeoni
 *
 */
public class BinderTests {

	DefaultEnvironment factory;
	
	@Before 
	@SuppressWarnings("unchecked")
	public void setup() {
		
		Resolver p = mock(Resolver.class);
		when(p.resolve(any(Key.class))).thenAnswer(new Answer<Kind<?>>() {
			public Kind<?> answer(InvocationOnMock invocation) throws Throwable {
				return ((Key)invocation.getArguments()[0]).kind();
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
	public void primitive() {
		
		TypeBinder<String> sb = new PrimitiveBinder<String>(String.class);
		String bound = sb.bind(v("hello"));
		
		assertEquals("hello",bound);
		
		try {
			sb.bind(v(3));
		}
		catch(RuntimeException e) {
			System.out.println("caught expected:"+e.getMessage());
		}
		
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
		
		TypeBinder<Integer> sb = new PrimitiveBinder<Integer>(Integer.class);
		
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
		
		TypeBinder<SomeInterface> b = new DefaultObjectBinder<SomeInterface>(get(Empty.class),factory);
		
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
			new DefaultObjectBinder<SomeInterface>(get(BadPlacement.class),factory);
			fail();
		}
		catch(Exception e) {
			System.out.println("caught expected:"+e.getMessage());
		}
		
		//ambiguous constructors
		try {
			new DefaultObjectBinder<SomeInterface>(get(TooManyConstructors.class),factory);
			fail();
		}
		catch(Exception e) {
			System.out.println("caught expected:"+e.getMessage());
		}
	}
	
	@Test 
	public void bindplacement() throws Exception {
		
		//on method
		TypeBinder<BindOnMethod> b1 = new DefaultObjectBinder<BindOnMethod>(get(BindOnMethod.class),factory);
		
		BindOnMethod r1 = b1.bind(s(p("a","hello")));
		assertNotNull(r1);
		assertTrue(r1.invoked);
		
		//on param
		TypeBinder<BindOnParam> b2 = new DefaultObjectBinder<BindOnParam>(get(BindOnParam.class),factory);
		
		BindOnParam r2 = b2.bind(s(p("a","hello")));
		assertNotNull(r2);
		assertTrue(r2.invoked);
		
		//on many param
		TypeBinder<BindOnManyParams> b3 = new DefaultObjectBinder<BindOnManyParams>(get(BindOnManyParams.class),factory);
		
		BindOnManyParams r3 = b3.bind(s(p("a","hello"),p("b","world")));
		assertNotNull(r3);
		assertTrue(r3.invoked);
		
	}

	@Test 
	public void primitiveField() throws Exception {
		
		TypeBinder<Primitive> b = new DefaultObjectBinder<Primitive>(get(Primitive.class),factory);
		
		Primitive r = b.bind(s(p("1",0),p("2",0),p("3",0),p("4",0),p("5",0),p("6",0),p("7",'c'),p("8",true)));
		assertNotNull(r);
		assertEquals(8,r.invoked.size());
	}
	
	@Test 
	public void multiparams() throws Exception {
	
		TypeBinder<MultiParams> b = new DefaultObjectBinder<MultiParams>(get(MultiParams.class),factory);
		
		MultiParams r = b.bind(s(p("a","hello"),p("b","world")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test 
	public void duplicateLabels() throws Exception {
	
		//ambiguous constructors
		try {
			new DefaultObjectBinder<SomeInterface>(get(DuplicateLabels.class),factory);
			fail();
		}
		catch(Exception e) {
			System.out.println("caught expected:"+e.getMessage());
		}
	}
	
	@Test 
	public void partialbinding() throws Exception {
	
		TypeBinder<Partial> b = new DefaultObjectBinder<Partial>(get(Partial.class),factory);
		
		Partial r = b.bind(s(p("a","hello"),p("b","world")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test 
	public void lax1() throws Exception {
	
		TypeBinder<Lax1> b = new DefaultObjectBinder<Lax1>(get(Lax1.class),factory);
		
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
	
		TypeBinder<Lax2> b = new DefaultObjectBinder<Lax2>(get(Lax2.class),factory);
		
		//a is wrongly typed
		Lax2 r = b.bind(s(p("a",3)));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test 
	public void throughInterface1() throws Exception {
	
		TypeBinder<InterfaceImpl1> b = new DefaultObjectBinder<InterfaceImpl1>(get(InterfaceImpl1.class),factory);
		
		//a is wrongly typed
		InterfaceImpl1 r = b.bind(s(p("a","hello")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test 
	public void throughInterface2() throws Exception {
	
		TypeBinder<InterfaceImpl2> b = new DefaultObjectBinder<InterfaceImpl2>(get(InterfaceImpl2.class),factory);
		
		//a is wrongly typed
		InterfaceImpl2 r = b.bind(s(p("b","hello")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}


	
	
	
	
	


}

