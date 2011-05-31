/**
 * 
 */
package org.ligo.core;

import static org.junit.Assert.*;
import static org.ligo.core.TestData.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ligo.core.Environment;
import org.ligo.core.Literal;
import org.ligo.core.TypeBinder;
import org.ligo.core.impl.DefaultEnvironment;
import org.ligo.core.impl.LigoResolver;

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
		env = new DefaultEnvironment(resolver);
	}
	
	@Test
	public void listtest() {
	
		TypeBinder<List<String>> b= env.binderFor(new Literal<List<String>>() {});
		
		List<String> r = b.bind(list(v("hello"),v("world")));
		assertNotNull(r);
		assertEquals(2, r.size());
		
	}
	
	@Test
	public void iterator() {
	
		TypeBinder<Iterator<String>> b= env.binderFor(new Literal<Iterator<String>>() {});
		
		Iterator<String> r = b.bind(list(v("hello"),v("world")));
		assertNotNull(r);
		while(r.hasNext())
			assertNotNull(r.next());
		
	}
	
	@Test
	public void array() {
	
		TypeBinder<String[]> b= env.binderFor(new Literal<String[]>() {});
		
		String[] r = b.bind(list(v("hello"),v("world")));
		assertNotNull(r);
		
		TypeBinder<List<String>[]> b1= env.binderFor(new Literal<List<String>[]>() {});
		
		List<String>[] r1 = b1.bind(list(v("hello"),v("world")));
		assertNotNull(r1);
		
		TypeBinder<Integer[]> b2= env.binderFor(new Literal<Integer[]>() {});
		
		Integer[] r2 = b2.bind(list(v(3),v(5)));
		assertNotNull(r2);
	}
}

