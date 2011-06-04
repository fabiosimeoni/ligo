/**
 * 
 */
package org.ligo.core;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.ligo.data.impl.DataBuilders.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ligo.core.TestClassDefs.ExplicitList;
import org.ligo.core.TestClassDefs.ExplicitNestedList;
import org.ligo.core.TestClassDefs.ImplicitList;
import org.ligo.core.TestClassDefs.ImplicitNestedList;
import org.ligo.core.binders.api.Environment;
import org.ligo.core.binders.api.TypeBinder;
import org.ligo.core.binders.impl.LigoEnvironment;
import org.ligo.core.keys.Literal;
import org.ligo.core.resolvers.impl.LigoResolver;
import org.ligo.data.LigoObject;

/**
 * @author Fabio Simeoni
 *
 */
public class CollectionBinderTests {

	LigoResolver resolver;
	Environment env;
	
	@Before
	public void setup() {

		resolver = new LigoResolver();
		env = new LigoEnvironment(resolver);
	}
	
	@Test
	public void list() {
	
		TypeBinder<List<String>> b= env.binderFor(new Literal<List<String>>() {});
		
		List<String> r = b.bind(d(v("hello"),v("world")));
		assertNotNull(r);
		assertEquals(2, r.size());
		
	}
	
	@Test
	public void implicitList() {
	
		TypeBinder<ImplicitList> b= env.binderFor(ImplicitList.class);
		
		//flat
		ImplicitList r = b.bind(o(n("a",v("hello")),n("a",v("world"))));
		assertNotNull(r);
		assertEquals(asList("hello","world"), r.l);
		
	}
	
	@Test
	public void explicitList() {
	
		TypeBinder<ExplicitList> b= env.binderFor(ExplicitList.class);
		
		//nested
		ExplicitList r = b.bind(
						o(
							n("list",o(
									  n("a",v("hello")),
									  n("a",v("world")))),
							n("shortlist",o(
									 n("a",v("ciao")),
										n("a",v("mondo"))))
						)
		);
		assertNotNull(r);
		assertEquals(asList("hello","world"), r.l);
		assertEquals(asList("ciao","mondo"), r.l2);
	}	
	
	@Test
	public void implicitNestedList() {
	
		TypeBinder<ImplicitNestedList> b= env.binderFor(ImplicitNestedList.class);
		
		LigoObject object = o(
								n("family",
										o(
										  n("person","john"),
										  n("person","anna"))),
								n("family",
										o(
										  n("person","paul"),
										  n("person","mary"))),
										  
								n("shortfamily",
											o(
											  n("person","john"),
											  n("person","anna"))),
								n("shortfamily",
											o(
											  n("person","paul"),
											  n("person","mary")))		  
										 
							);
		
		System.out.println("data="+object);
		ImplicitNestedList r = b.bind(object);
		
		assertNotNull(r);
		assertEquals(2, r.families.size());
		for (List<String> family : r.families) {
			assertEquals(2, family.size());
			System.out.println(family);
		}
		assertEquals(2, r.families2.size());
		for (List<String> family : r.families2) {
			assertEquals(2, family.size());
			System.out.println(family);
		}
		
	}
	
	@Test
	public void explicitNestedList() {
	
		TypeBinder<ExplicitNestedList> b= env.binderFor(ExplicitNestedList.class);
		
		LigoObject object = o(
								n("families",o(
											n("noise","noise"),
											n("family",
													o(
													  n("person","john"),
													  n("noise","noise"),
													  n("person","anna"))),
											n("family",
													o(
													  n("person","paul"),
													  n("person","mary"))))),
													  n("noise","noise"),
								n("shortfamilies",o(
										n("family",
												o(
												  n("person","john"),
												  n("person","anna"))),
										n("family",
												o(
												  n("person","paul"),
												  n("person","mary"))))),
								n("noise","noise")
							);
		
		System.out.println("data="+object);
		ExplicitNestedList r = b.bind(object);
		
		assertNotNull(r);
		assertEquals(2, r.families.size());
		for (List<String> family : r.families) {
			assertEquals(2, family.size());
			System.out.println(family);
		}
		assertEquals(2, r.families2.size());
		for (List<String> family : r.families2) {
			assertEquals(2, family.size());
			System.out.println(family);
		}
		
	}
	
	@Test
	public void iterator() {
	
		TypeBinder<Iterator<String>> b= env.binderFor(new Literal<Iterator<String>>() {});
		
		Iterator<String> r = b.bind(d(v("hello"),v("world")));
		assertNotNull(r);
		while(r.hasNext())
			assertNotNull(r.next());
		
	}

	@Test
	public void array1() {
	
		TypeBinder<String[]> b= env.binderFor(new Literal<String[]>() {});
		
		String[] r = b.bind(d(v("hello"),v("world")));
		assertNotNull(r);
		
	}
	
	@Test
	public void array2() {
	
		TypeBinder<List<String>[]> b1= env.binderFor(new Literal<List<String>[]>() {});
		
		List<String>[] r1 = b1.bind(d(v("hello"),v("world")));
		assertNotNull(r1);
		
	}
	
	@Test
	public void array3() {
	
		TypeBinder<Integer[]> b2= env.binderFor(new Literal<Integer[]>() {});
		
		Integer[] r2 = b2.bind(d(v(3),v(5)));
		assertNotNull(r2);
	}
}

