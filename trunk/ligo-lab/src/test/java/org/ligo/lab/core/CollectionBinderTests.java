/**
 * 
 */
package org.ligo.lab.core;

import static org.junit.Assert.*;
import static org.ligo.lab.core.TestData.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ligo.lab.core.impl.DefaultEnvironment;
import org.ligo.lab.core.impl.LigoResolver;

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
}

