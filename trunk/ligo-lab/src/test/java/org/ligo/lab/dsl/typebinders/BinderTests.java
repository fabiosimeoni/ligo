/**
 * 
 */
package org.ligo.lab.dsl.typebinders;

import static org.junit.Assert.*;
import static org.ligo.lab.typebinders.Key.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ligo.lab.typebinders.Bind;
import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.TypeBinder;
import org.ligo.lab.typebinders.Literal;
import org.ligo.lab.typebinders.Resolver;
import org.ligo.lab.typebinders.impl.DefaultObjectBinder;
import org.ligo.lab.typebinders.impl.DefaultEnvironment;
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
		
		factory = new DefaultEnvironment(p);
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
		
		public void poo(@Bind("foo3") String s){}
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
		
		void foo(@Bind("foo") T s);
	}
	
	static class Raw<T> implements IRaw<T> {
		
		Raw(@Bind("foo2") String s) {};
		public void foo(T s) {};
	}
	
	static class RawString extends Raw<String> {
		RawString(String s) {super(s);}
	}
	
	
}

