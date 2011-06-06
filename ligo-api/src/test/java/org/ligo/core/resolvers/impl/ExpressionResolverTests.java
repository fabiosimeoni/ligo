/**
 * 
 */
package org.ligo.core.resolvers.impl;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.ligo.core.resolvers.impl.LigoExpressionResolver.*;
import static org.ligo.core.utils.Constants.*;
import static org.ligo.data.impl.DataBuilders.*;

import java.util.List;
import java.util.regex.Matcher;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.ligo.core.binders.api.Environment;
import org.ligo.core.binders.impl.LigoEnvironment;
import org.ligo.data.LigoData;
import org.ligo.data.LigoObject;

/**
 * @author Fabio Simeoni
 *
 */
public class ExpressionResolverTests {

	LigoResolver resolver;
	Environment env;
	
	
	String lbl = "foo";
	String path = "foo/boo";
	String coll = "foo[boo]";
	String collpath = "foo[boo/goo]";
	String pathcoll = "foo/boo[goo]";
	String nestedcoll = "foo[boo[goo/noo]]";
	String implicitcoll = "[]";
	String emptycoll = "foo[]";
	String emptynestedcoll = "foo[boo/goo[]]";
	
	@Before
	public void setup() {

		resolver = new LigoResolver();
		env = new LigoEnvironment(resolver);
	}
	
	@Test
	public void collpattern() {
		
	
		Matcher collmatcher = COLLEXP_PATTERN.matcher(lbl);
		assertFalse(collmatcher.matches());
		
		collmatcher = COLLEXP_PATTERN.matcher(path);
		assertFalse(collmatcher.matches());
		
		collmatcher = COLLEXP_PATTERN.matcher(coll);
		assertTrue(collmatcher.matches());
		assertEquals("foo",collmatcher.group(1));
		assertEquals("boo",collmatcher.group(2));
		
		collmatcher = COLLEXP_PATTERN.matcher(pathcoll);
		assertTrue(collmatcher.matches());
		assertEquals("foo/boo",collmatcher.group(1));
		assertEquals("goo",collmatcher.group(2));
		
		collmatcher = COLLEXP_PATTERN.matcher(collpath);
		assertTrue(collmatcher.matches());
		assertEquals("foo",collmatcher.group(1));
		assertEquals("boo/goo",collmatcher.group(2));
		
		collmatcher = COLLEXP_PATTERN.matcher(nestedcoll);
		assertTrue(collmatcher.matches());
		assertEquals("foo",collmatcher.group(1));
		assertEquals("boo[goo/noo]",collmatcher.group(2));
		
		collmatcher = COLLEXP_PATTERN.matcher(implicitcoll);
		assertTrue(collmatcher.matches());
		assertTrue(collmatcher.group(1).isEmpty());
		assertTrue(collmatcher.group(2).isEmpty());
		
	
		
		collmatcher = COLLEXP_PATTERN.matcher(emptycoll);
		assertTrue(collmatcher.matches());
		assertEquals("foo",collmatcher.group(1));
		assertTrue(collmatcher.group(2).isEmpty());
		
		collmatcher = COLLEXP_PATTERN.matcher(emptynestedcoll);
		assertTrue(collmatcher.matches());
		assertEquals("foo",collmatcher.group(1));
		assertEquals("boo/goo[]",collmatcher.group(2));
	}
	
	@Test
	public void pathpattern() {
		
	
		Matcher matcher = PATHEXP_PATTERN.matcher(lbl);
		assertTrue(matcher.matches());
		assertEquals("foo",matcher.group(1));
		
		matcher = PATHEXP_PATTERN.matcher(path);
		assertTrue(matcher.matches());
		assertEquals("foo",matcher.group(2));
		assertEquals("boo",matcher.group(3));
		
		matcher = PATHEXP_PATTERN.matcher(coll);
		assertFalse(matcher.matches());
		
		matcher = PATHEXP_PATTERN.matcher(pathcoll);
		assertTrue(matcher.matches());
		assertEquals("foo",matcher.group(2));
		assertEquals("boo[goo]",matcher.group(3));
		
		matcher = PATHEXP_PATTERN.matcher(collpath);
		assertFalse(matcher.matches());
		
		matcher = PATHEXP_PATTERN.matcher(nestedcoll);
		assertFalse(matcher.matches());
		
		matcher = PATHEXP_PATTERN.matcher(implicitcoll);
		assertFalse(matcher.matches());
		
		matcher = PATHEXP_PATTERN.matcher(emptycoll);
		assertFalse(matcher.matches());
		
		matcher = PATHEXP_PATTERN.matcher(emptynestedcoll);
		assertFalse(matcher.matches());
	}
	
	@Test
	public void path() {
		
		LigoObject lo = o(
							n("1",
								o(n("2",
										o(n("3",1))))),
							n("1",
								o(n("2",
										o(n("3",2))))),
							n("1","stopshort"),
							n("1",o(n("2","stopshort"))),
							n("other",2));
		
		LigoExpressionResolver r = new LigoExpressionResolver();
		
		List<? extends LigoData> ps =  r.resolve(new QName("1/2/3"), lo);
		assertEquals(d(v(1),v(2)), ps);
		
		ps =  r.resolve(new QName("1/(.*)"), lo);
		assertEquals(3,ps.size());
		
		ps =  r.resolve(new QName("(.*)"), lo);
		assertEquals(5,ps.size());
		
		ps =  r.resolve(new QName("1"), lo);
		assertEquals(4,ps.size());
		
	}
	
	@Test
	public void groups() {
		
		LigoObject lo = o(
							n("a",o(
									n("b",1),
									n("b",2))),
									
							n("extra",o(
									n("b",3),
									n("b",4))),
							n("a",o(
									n("d",5),
									n("d",6))),
							n("a",o(
									n("b",7),
									n("b",8))));
		
		LigoExpressionResolver r = new LigoExpressionResolver();
		
		List<? extends LigoData> ps =  r.resolve(new QName("[]"), lo);
		List<LigoObject> expected = asList(
						o(n(NONAME,1),n(NONAME,2)),
						o(n(NONAME,3),n(NONAME,4)),
						o(n(NONAME,5),n(NONAME,6)),
						o(n(NONAME,7),n(NONAME,8)));

		ps =  r.resolve(new QName("a[]"), lo);
		expected = asList(
				o(n(NONAME,1),n(NONAME,2)),
				o(n(NONAME,5),n(NONAME,6)),
				o(n(NONAME,7),n(NONAME,8)));
		assertEquals(expected, ps);
		
		ps =  r.resolve(new QName("[b]"), lo);
		assertEquals(3, ps.size());
		
		ps =  r.resolve(new QName("a[b]"), lo);
		expected = asList(
				o(n(NONAME,1),n(NONAME,2)),
				o(n(NONAME,7),n(NONAME,8)));
		assertEquals(expected, ps);
		

		
		
	}
}

