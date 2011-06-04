/**
 * 
 */
package org.ligo.json;

import static java.util.Arrays.*;
import static junit.framework.Assert.*;
import static org.ligo.core.utils.Constants.*;
import static org.ligo.data.impl.DataBuilders.*;
import static org.ligo.dsl.Ligo.*;
import static org.ligo.json.JSonBinders.*;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;
import org.ligo.core.binders.api.Binder;
import org.ligo.core.binders.impl.LigoEnvironment;
import org.ligo.core.resolvers.impl.LigoResolver;
import org.ligo.data.LigoData;

/**
 * @author Fabio Simeoni
 *
 */
public class JSONTests {

	LigoResolver resolver;
	LigoEnvironment env;
	
	
	@Before
	public void setup() {

		resolver = new LigoResolver();
		env = new LigoEnvironment(resolver);
	}
	
	
	@Test 
	public void jacksonparser() throws Exception {
		
		LigoData data = JSON_READER.bind(new StringReader("10"));
		assertEquals(v("10"), data);
		System.out.println("plain value (ok) "+data);
			
		data = JSON_READER.bind(new StringReader("{}"));
		assertEquals(o(), data);
		System.out.println("empty object (ok) "+data);
		
			
		data = JSON_READER.bind(new StringReader("[]"));
		assertEquals(o(), data);
		System.out.println("empty array (ok) "+data);
		
		data = JSON_READER.bind(new StringReader("[[]]"));
		assertEquals(o(n(NONAME,o())), data);
		System.out.println("empty nested array (ok) "+data);
		
		data = JSON_READER.bind(new StringReader("{\"a\":\"hello\",\"b\":\"world\"}"));
		assertEquals(o(n("a","hello"),n("b","world")), data);
		System.out.println("map (ok) "+data);
		
		data = JSON_READER.bind(new StringReader("[1,2,3]"));
		assertEquals(o(n(NONAME,"1"),n(NONAME,"2"),n(NONAME,"3")), data);
		System.out.println("array (ok) "+data);

		data = JSON_READER.bind(new StringReader("[[1,2]]"));
		assertEquals(o(n(NONAME,o(n(NONAME,"1"),n(NONAME,"2")))), data);
		System.out.println("nested array (ok) "+data);
			
		data = JSON_READER.bind(new StringReader("{\"a\":{}}"));
		assertEquals(o(n("a",o())), data);
		System.out.println("empty object field (ok) "+data);
			
		data = JSON_READER.bind(new StringReader("{\"a\":[]}"));
		assertEquals(o(n("a",o())), data);
		System.out.println("empty array field (ok) "+data);
			
		String json= "{\"id\":1125687077,\"foo\":\"hello\",\"boo\":{\"goo\":true}, \"array\":[3,5]}";
		data = JSON_READER.bind(new StringReader(json));
		assertEquals(o(n("id","1125687077"),
					   n("foo","hello"), 
					   n("boo",o(n("goo","true"))),
					   n("array",o(n(NONAME,"3"),n(NONAME,"5")))), data);
		System.out.println("document (ok) "+data);
	}
	
	
	@Test
	@SuppressWarnings("unchecked")
	public void json() throws Exception {
		
		Binder<Reader,Managed> binder = bind(Managed.class).in(env).and(JSON_READER).build();

		String json = "{ " +
		"			\"p1\" : \"hello\", " +
		"			\"p2\" : " +
		"					{\"p\":\"hello\"}," +
		"			\"p3\" : " +
		"					[  \"hello\", \"world\" ]," +
		"			\"p4\" : " +
		"					[  {\"p\":\"hello\"}, {\"p\":\"world\"} ]," +
		"			\"p5\" :[  " +
		"						[\"hello\", \"world\"], " +
		"						[\"ciao\", \"mondo\"] " +
		"					]," +
		"			\"p6\" : [  " +
		"						[ "+
		"						   {\"p\":\"hello\"}, " +
		"						   {\"p\":\"world\"} " +
		"						], " +
		"					 	[" +
		"						   {\"p\":\"ciao\"}, " +
		"						   {\"p\":\"mondo\"} " +
		"						] " +
		"					  ] ," +
		"			\"p7\" : [  " +
		"							{\"g\":{\"p\":\"hello\"}}, " +
		"							{\"g\":{\"p\":\"world\"}} " +
		"					] " +			
		"		}";

		
		Managed m = binder.bind(new StringReader(json));
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
		
		assertEquals(asList(new Generic(new Dependency("hello")), new Generic(new Dependency("world"))), m.p7);
	}
}
