/**
 * 
 */
package org.ligo.core;

import static org.junit.Assert.*;
import static org.ligo.core.binders.api.BindMode.*;
import static org.ligo.data.impl.DataBuilders.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ligo.core.TestClassDefs.Adapted;
import org.ligo.core.TestClassDefs.Adapted2;
import org.ligo.core.TestClassDefs.BadPlacement;
import org.ligo.core.TestClassDefs.BindOnConstructor;
import org.ligo.core.TestClassDefs.BindOnManyParams;
import org.ligo.core.TestClassDefs.BindOnMethod;
import org.ligo.core.TestClassDefs.BindOnParam;
import org.ligo.core.TestClassDefs.DuplicateLabels;
import org.ligo.core.TestClassDefs.Empty;
import org.ligo.core.TestClassDefs.InterfaceImpl1;
import org.ligo.core.TestClassDefs.InterfaceImpl2;
import org.ligo.core.TestClassDefs.Lax1;
import org.ligo.core.TestClassDefs.Lax2;
import org.ligo.core.TestClassDefs.MultiParams;
import org.ligo.core.TestClassDefs.Partial;
import org.ligo.core.TestClassDefs.Primitive;
import org.ligo.core.TestClassDefs.SomeInterface;
import org.ligo.core.TestClassDefs.TooManyConstructors;
import org.ligo.core.binders.api.Environment;
import org.ligo.core.binders.api.TypeBinder;
import org.ligo.core.binders.impl.LigoEnvironment;
import org.ligo.core.resolvers.impl.LigoResolver;
import org.ligo.data.LigoData;

/**
 * @author Fabio Simeoni
 *
 */
public class BinderTests {

	LigoResolver resolver;
	Environment env;
	
	@Before
	public void setun() {

		resolver = new LigoResolver();
		env = new LigoEnvironment(resolver);
	}
	
	@Test
	public void primitive() {
		
		TypeBinder<String> sb = env.binderFor(String.class);
		String bound = sb.bind(v("hello"));
		
		assertEquals("hello",bound);
		
		sb.bind(v(3));
		
		try {
			sb.bind(o(n("1",v("hello"))));
			fail();
		}
		catch(RuntimeException e) {
			System.out.println("caught expected:"+e.getMessage());
		}
		
		List<LigoData> ps = d(v("hello"),v("world"));
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
		
		bound = sb.bind(o(n("1",v("hello"))));
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
		
		SomeInterface r = b.bind(o());
		assertNotNull(r);
	}

	@Test 
	public void badConstructoro() {
	
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
	public void bindplacement() {
		
		//on constructor
		TypeBinder<BindOnConstructor> b0 = env.binderFor(BindOnConstructor.class);
		
		BindOnConstructor r0 = b0.bind(o(n("a","hello")));
		assertNotNull(r0);
		
		//on method
		TypeBinder<BindOnMethod> b1 = env.binderFor(BindOnMethod.class);
		
		BindOnMethod r1 = b1.bind(o(n("a","hello")));
		assertNotNull(r1);
		assertTrue(r1.invoked);
		
		//on param
		TypeBinder<BindOnParam> b2 = env.binderFor(BindOnParam.class);;
		
		BindOnParam r2 = b2.bind(o(n("a","hello")));
		assertNotNull(r2);
		assertTrue(r2.invoked);
		
		//on many param
		TypeBinder<BindOnManyParams> b3 = env.binderFor(BindOnManyParams.class);
		
		BindOnManyParams r3 = b3.bind(o(n("a","hello"),n("b","world")));
		assertNotNull(r3);
		assertTrue(r3.invoked);
		
	}

	@Test 
	public void primitiveField() {
		
		TypeBinder<Primitive> b = env.binderFor(Primitive.class);;
		
		Primitive r = b.bind(o(n("1",0),n("2",0),n("3",0),n("4",0),n("5",0),n("6",0),n("7",'c'),n("8",true)));
		assertNotNull(r);
		assertEquals(8,r.invoked.size());
	}
	
	@Test 
	public void multiparamo() {
	
		TypeBinder<MultiParams> b = env.binderFor(MultiParams.class);
		
		MultiParams r = b.bind(o(n("a","hello"),n("b","world")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test 
	public void duplicateLabelo() {
	
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
	public void partialbinding() {
	
		TypeBinder<Partial> b = env.binderFor(Partial.class);
		
		Partial r = b.bind(o(n("a","hello"),n("b","world")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test 
	public void lax1() {
	
		TypeBinder<Lax1> b = env.binderFor(Lax1.class);
		
		//a is missing, b is wrongly typed
		Lax1 r = b.bind(o(n("b","world")));
		assertNotNull(r);
		assertTrue(r.invoked1);
		assertTrue(r.invoked2);
		
		//all is missing
		r = b.bind(o());
		assertNotNull(r);
		assertTrue(r.invoked1);
		assertTrue(r.invoked2);
	}
	
	@Test 
	public void lax2() {
	
		TypeBinder<Lax2> b = env.binderFor(Lax2.class);
		
		//a is wrongly typed
		Lax2 r = b.bind(o(n("a",3)));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test 
	public void throughInterface1() {
	
		TypeBinder<InterfaceImpl1> b = env.binderFor(InterfaceImpl1.class);
		
		//a is wrongly typed
		InterfaceImpl1 r = b.bind(o(n("a","hello")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test 
	public void throughInterface2() {
	
		TypeBinder<InterfaceImpl2> b = env.binderFor(InterfaceImpl2.class);
		
		//a is wrongly typed
		InterfaceImpl2 r = b.bind(o(n("b","hello")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	
	@Test 
	public void adapted() {
	
		TypeBinder<Adapted> b = env.binderFor(Adapted.class);
		
		//a is wrongly typed
		Adapted r = b.bind(o(n("a","hello")));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test 
	public void adapted2() {
	
		TypeBinder<Adapted2> b = env.binderFor(Adapted2.class);
		
		//a is wrongly typed
		Adapted2 r = b.bind(o(n("person",o(n("name","John"),n("age",20)))));
		assertNotNull(r);
		assertTrue(r.invoked);
	}
	
	@Test
	public void interfaceBinding() {

		resolver.bind(SomeInterface.class, BindOnManyParams.class);
		resolver.bind(SomeInterface.class, Empty.class);
		
		TypeBinder<SomeInterface> b = env.binderFor(SomeInterface.class);
		
		SomeInterface r = b.bind(o(n("a","hello"),n("c","world")));
		assertNotNull(r);
		assertTrue(r instanceof Empty);
	}

}

