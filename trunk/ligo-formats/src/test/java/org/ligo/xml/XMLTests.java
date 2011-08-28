/**
 * 
 */
package org.ligo.xml;

import static java.util.Arrays.*;
import static junit.framework.Assert.*;
import static org.ligo.core.utils.Constants.*;
import static org.ligo.data.impl.DataBuilders.*;
import static org.ligo.dsl.Ligo.*;
import static org.ligo.xml.XMLBinders.*;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;
import org.ligo.core.binders.api.Binder;
import org.ligo.core.binders.impl.LigoEnvironment;
import org.ligo.core.resolvers.impl.LigoResolver;
import org.ligo.data.LigoData;
import org.ligo.xml.Managed.Structure;

/**
 * @author Fabio Simeoni
 *
 */
public class XMLTests {

	LigoResolver resolver;
	LigoEnvironment env;
	
	
	@Before
	public void setup() {

		resolver = new LigoResolver();
		env = new LigoEnvironment(resolver);
	}
	
	
	@Test 
	public void domparser() throws Exception {
		
		LigoData data = XREADER.bind(new StringReader("<d/>"));
		assertEquals(o(), data);
		System.out.println("empty element (ok) "+data);

		data = XREADER.bind(new StringReader("<d><a b='hello'/></d>"));
		assertEquals(o(n("a",o(n("b","hello")))), data);
		System.out.println("attributed empty element (ok) "+data);
		
		data = XREADER.bind(new StringReader("<d>hello</d>"));
		assertEquals(v("hello"), data);
		System.out.println("simple element (ok) "+data);
		
		data = XREADER.bind(new StringReader("<d a='hello'>world</d>"));
		assertEquals(o(n("a","hello"),n(NONAME,"world")), data);
		System.out.println("attributed simple element (ok) "+data);

		data = XREADER.bind(new StringReader("<d><a>hello</a><b>world</b></d>"));
		assertEquals(o(n("a","hello"),n("b","world")), data);
		System.out.println("complex element (ok) "+data);
		
		data = XREADER.bind(new StringReader("<d a='hello'><a>world</a></d>"));
		assertEquals(o(n("a","hello"),n("a","world")), data);
		System.out.println("complex attributed element (ok) "+data);

		data = XREADER.bind(new StringReader("<d><a/></d>"));
		assertEquals(o(n("a",o())), data);
		System.out.println("inner empty element (ok) "+data);

		data = XREADER.bind(new StringReader(
				"<d id='1125687077' foo='hello'>" +
				"  <boo goo='true'/>" +
				"  <array>" +
				"		<element>3</element>" +
				"  		<element>5</element>" +
				" </array>	" +
				"</d>"));
		assertEquals(o(n("id","1125687077"),
				   n("foo","hello"), 
				   n("boo",o(n("goo","true"))),
				   n("array",o(n("element","3"),n("element","5")))), data);
		System.out.println("sample document (ok) "+data);

		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void xml() throws Exception {
		
		Binder<Reader,Managed> binder = bind(Managed.class).in(env).and(XREADER).build();
		
		String xml = "<d p1='hello'> " +
					"			<p2 p='hello'/>" +
					"			<p3>" +
					"				<el>hello</el><el>world</el>" +
					"			</p3>" +
					"			<p4>" +
					"				<el p='hello'/><el p='world'/>" +
					"			</p4>" +
					"			<p5> " +
					"					<el>" +
					"						<el>hello</el>" +
					"						<el>world</el>" +
					"					</el> " +
					"					<el>" +
					"						<el>ciao</el>" +
					"						<el>mondo</el>" +
					"					</el> " +
					"			</p5>"+
					"			<p6> " +
					"					<el>" +
					"						<el p='hello'/>" +
					"						<el p='world'/>" +
					"					</el> " +
					"					<el>" +
					"						<el p='ciao'/>" +
					"						<el p='mondo'/>" +
					"					</el> " +
					"			</p6> "+
					"           <p7>  " +
					"					<el><g p='hello'/></el>" +
					"					<el><g p='world'/></el>" +
					"			</p7>" +
					"            <p8 attr='hello'>world</p8>"+
					"		</d>";
		
		Managed m = binder.bind(new StringReader(xml));
		assertEquals("hello",m.p1);
		assertEquals(new Dependency("hello"),m.p2);
		assertEquals(asList("hello","world"),m.p3);
		assertEquals(asList(new Dependency("hello"),new Dependency("world")),m.p4);
		
		assertEquals(asList(
				asList("hello","world"),
				asList("ciao","mondo")),m.p5);
		
		assertEquals(asList(
				asList(new Dependency("hello"),new Dependency("world")),
				asList(new Dependency("ciao"),new Dependency("mondo"))),m.p6);
		
		assertEquals(asList(new Generic<Dependency>(new Dependency("hello")), new Generic<Dependency>(new Dependency("world"))), m.p7);
		
		assertEquals(new Structure("hello","world"), m.p8);
		
	}
}
