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
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ligo.lab.data.Provided;
import org.ligo.lab.typebinders.Bind;
import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.Literal;
import org.ligo.lab.typebinders.Resolver;
import org.ligo.lab.typebinders.TypeBinder;
import org.ligo.lab.typebinders.impl.DefaultEnvironment;
import org.ligo.lab.typebinders.impl.DefaultObjectBinder;
import org.ligo.lab.typebinders.impl.PrimitiveBinders;
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
		
		TypeBinder<String> sb = new PrimitiveBinders.StringBinder();
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
		
		TypeBinder<Integer> sb = new PrimitiveBinders.IntBinder();
		
		int bound = sb.bind(v(1));
		assertEquals(1,bound);
		
		bound = sb.bind(v("1"));
		assertEquals(1,bound);
		
		try {
			sb.bind(v("hello"));
		}
		catch(RuntimeException e) {
			System.out.println("caught expected:"+e.getMessage());
		}
		
		sb.setMode(LAX);
		
		bound = sb.bind(v("hello"));
		assertEquals(0,bound);
		
		
	}
		
	@Test 
	public void object() throws Exception {
	
		TypeBinder<SomeInterface> ob = new DefaultObjectBinder<SomeInterface>(get(Empty.class),factory);
		
		try{
			ob.bind(v(5));
			fail();
		}
		catch(Exception e) {
			System.out.println("caught expected:"+e.getMessage());
		}
		
		SomeInterface result = ob.bind(s());
		assertNotNull(result);
		
		//badly annotated constructor
		try {
			new DefaultObjectBinder<SomeInterface>(get(BadConstructor.class),factory);
		}
		catch(Exception e) {
			System.out.println("caught expected:"+e.getMessage());
		}
		
		TypeBinder<GoodMethod> gc = new DefaultObjectBinder<GoodMethod>(get(GoodMethod.class),factory);
		
		GoodMethod gcresult = gc.bind(s(p("a","hello")));
		assertNotNull(gcresult);
		assertTrue(gcresult.invoked);
		
		TypeBinder<GoodMethod2> gc2 = new DefaultObjectBinder<GoodMethod2>(get(GoodMethod2.class),factory);
		
		GoodMethod2 gc2result = gc2.bind(s(p("a","hello")));
		assertNotNull(gc2result);
		assertTrue(gc2result.invoked);

		TypeBinder<MultiParams> mp = new DefaultObjectBinder<MultiParams>(get(MultiParams.class),factory);
		
		MultiParams mpresult = mp.bind(s(p("a","hello"),p("b","world")));
		assertNotNull(mpresult);
		assertTrue(mpresult.invoked);
		
		TypeBinder<Partial> pb = new DefaultObjectBinder<Partial>(get(Partial.class),factory);
		
		Partial pbresult = pb.bind(s(p("a","hello"),p("c","world")));
		assertNotNull(pbresult);
		assertTrue(pbresult.invoked);
	}
	

	interface SomeInterface {}

	//un-annotated class
	static class Empty implements SomeInterface {}
	
	//standard class
	static class BadConstructor implements SomeInterface {
		@Bind("a")
		public BadConstructor() {}
	}
	
	//standard class
	static class TooManyConstructors implements SomeInterface {
		
		@Bind("a")
		public TooManyConstructors(Integer i) {}
		
		@Bind("b")
		public TooManyConstructors(String s) {}
	}
	
	//standard class
	static class GoodMethod implements SomeInterface {
		
		boolean invoked;
		
		@Bind("a")
		public void m(String s) {invoked=true;}
	}
	
	//standard class
	static class GoodMethod2 implements SomeInterface {
		
		boolean invoked;
		
		public void m(@Bind("a") String s) {invoked=true;}
	}

	//standard class
	static class MultiParams implements SomeInterface {
		
		boolean invoked;
		
		public void m(@Bind("a") String s1, @Bind("b") String s2) {invoked=true;}
	}
	
	//standard class
	static class Partial implements SomeInterface {
		
		boolean invoked;
		
		public void m(@Bind("a") String s1, String s2, @Bind("c") String s3) {
			assertNull(s2);
			invoked=true;
		}
	}
	
	//standard class
	static class BadPartial implements SomeInterface {
		
		boolean invoked;
		
		public void m(@Bind("a") String s1, int s2, @Bind("c") String s3) {
			assertEquals(0,s2);
			invoked=true;
		}
	}
	
	
	
	
	
	
	
	
	@Test
	public void badinputs() {
		
		try {
			Literal<List<String>> lit = new Literal<List<String>>() {};
			new DefaultObjectBinder<List<String>>(get(lit), factory);
			fail();
		}
		catch(RuntimeException e) {
			System.out.println("caught expected:"+e.getMessage());
		}
		
		try {
			new DefaultObjectBinder<BadlyManaged>(get(BadlyManaged.class),factory); 
			fail();
		}
		catch(RuntimeException e) {
			System.out.println("caught expected:"+e.getMessage());
		}
		
		try {
			new DefaultObjectBinder<BadlyManaged2>(get(BadlyManaged2.class),factory); 
			fail();
		}
		catch(RuntimeException e) {
			System.out.println("caught expected:"+e.getMessage());
		}

		
	}
	
	@Test
	public void raw() {
		
		TypeBinder<?> binder = new DefaultObjectBinder<Nullary>(get(Nullary.class), factory); 
		
		binder = new DefaultObjectBinder<Managed>(get(Managed.class),factory); 
		
		System.out.println(binder);
		
	}
	
	@Test
	public void generics() {
		
		Literal<Raw<String>> lit = new Literal<Raw<String>>() {};
		TypeBinder<Raw<String>> binder = new DefaultObjectBinder<Raw<String>>(get(lit), factory); 
				
		System.out.println(binder);
	
		TypeBinder<RawString> binder2 = new DefaultObjectBinder<RawString>(get(RawString.class), factory); 
		
		System.out.println(binder2);
	}
	
		interface IManaged<T> {
		
		void poo(@Bind("foo3") T s);
		
	}
	static class Managed implements IManaged<String> {
		
		Managed(@Bind("foo") String s) {};
		
		void foo(@Bind("foo2") String s){}
		void goo(@Bind("foo3") List<String> s){}
		
		public void poo(@Bind("foo4") String s){}
		
		public void noo(@Bind("foo5") Iterator<String> s){}
	}
	
	static class BadlyManaged {
		
		BadlyManaged(@Bind("foo") String s) {};
		BadlyManaged(@Bind("foo2") int s) {};
	}
	
	static class BadlyManaged2 {
		
		BadlyManaged2(@Bind("foo") String s) {};
		void foo(@Bind("foo") String s){}
	}
	
	static class Nullary {}
	
	static interface IRaw<T> {
		
		void foo(@Bind(".") T s);
	}
	
	static class Raw<T> implements IRaw<T> {
		
		Raw(@Bind("foo2") String s) {};
		public void foo(T s) {};
	}
	
	static class RawString extends Raw<String> {
		RawString(String s) {super(s);}
	}
	
	
	//////////////////////////////////////////////////////////////////
	


}

