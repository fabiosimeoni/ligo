/**
 * 
 */
package org.ligo.nodes.bindings;

import static junit.framework.Assert.*;
import static org.ligo.core.utils.Constants.*;
import static org.ligo.data.impl.DataBuilders.*;
import static org.ligo.dsl.Ligo.*;
import static org.ligo.json.JSonBinders.*;
import static org.ligo.xml.XMLBinders.*;

import java.io.Reader;
import java.io.StringReader;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.ligo.core.binders.api.Binder;
import org.ligo.core.binders.impl.LigoEnvironment;
import org.ligo.core.resolvers.impl.LigoResolver;
import org.ligo.data.LigoData;
import org.ligo.data.LigoObject;

/**
 * @author Fabio Simeoni
 *
 */
public class BindingTests {

	LigoResolver resolver;
	LigoEnvironment env;
	
	
	@Before
	public void setup() {

		resolver = new LigoResolver();
		env = new LigoEnvironment(resolver);
	}
	
	
	@Test
	public void memory() {
		
		resolver.bind(ManagedDep.class, DepImpl.class);
		
		Binder<LigoData,Managed> binder = bind(Managed.class).in(env).build();
		
		LigoObject object = o(
				n("p1","hello"),
				n("p2",o(n("attr","whole wide"),n(NONAME,"world"))),
				n("p3",o(n("p1",10))),
				n("p4","hello"),
				n("p4","world"),
				n("p5","hello"),
				n("p5","world"),
				n("p6",o(n("p1","hello")))
		);
		
		Managed m = binder.bind(object);
		System.out.println(object);
		assertNotNull(m);
	}
	
	@Test
	public void xml() throws Exception {
		
		resolver.bind(ManagedDep.class, DepImpl.class);
		
		Binder<Reader,Managed> binder = bind(Managed.class).in(env).and(XREADER).build();
		
		String xml = "<doc>" +
				"		<p1>hello</p1>" +
				"		<p2  attr='whole wide'>world</p2>" +
				"		<p3>" +
				"			<p1>10</p1>" +
				"		</p3>" +
				"		<p4>hello</p4>" +
				"       <p4>world</p4>" +
				"		<p5>hello</p5>" +
				"		<p5>world</p5>" +
				"		<p6>" +
				"         <p1>hello</p1>" +
				"		</p6>" +
				"	</doc>";
		
		System.out.println(XREADER.bind(new StringReader(xml)));
		Managed m = binder.bind(new StringReader(xml));
		assertNotNull(m);
	}
	
	@Test
	public void json() throws Exception {
		
		resolver.bind(ManagedDep.class, DepImpl.class);
		
		Binder<JSONObject,Managed> binder = bind(Managed.class).in(env).and(JSON_OBJECT).build();

		String json = "{ " +
		"			p1 : hallo, " +
		"			p2 : {attr : \"whole wide\","+NONAME+" : world}, " +
		"			p3 : { p1 : 10 }," +
		"			p4 : [  hello, world ]," +
		"			p5 : [  hello, world ]," +
		"			p6 : { p1 : hello } " +			
		"		}";

		
		Managed m = binder.bind(new JSONObject(json));
		assertNotNull(m);		
	}
		
	
	@Test 
	public void jackson() throws Exception {
		
		JsonFactory jsonFactory = new JsonFactory();
		
		JsonParser parser = jsonFactory.createJsonParser(new StringReader("10"));
		System.out.println("value=>"+parse(parser));
			
		parser = jsonFactory.createJsonParser(new StringReader("{}"));
		System.out.println("empty object=>"+parse(parser));
			
		parser = jsonFactory.createJsonParser(new StringReader("[]"));
		System.out.println("empty array=>"+parse(parser));
			
		parser = jsonFactory.createJsonParser(new StringReader("[1,2,3]"));
		System.out.println("array=>"+ parse(parser));
			
		parser = jsonFactory.createJsonParser(new StringReader("{\"a\":{}}"));
		System.out.println("empty object field=>"+parse(parser));
			
		parser = jsonFactory.createJsonParser(new StringReader("{\"a\":[]}"));
		System.out.println("empty array field=>"+parse(parser));
			
		String json= "{\"id\":1125687077,\"foo\":\"hello\",\"boo\":{\"goo\":true}, \"array\":[3,5]}";
		parser = jsonFactory.createJsonParser(new StringReader(json));
		System.out.println("full object"+parse(parser));
	}
	
	
	@Test
	public void json2() throws Exception {
		
		resolver.bind(ManagedDep.class, DepImpl.class);
		
		Binder<Reader,ManagedJSON> binder = bind(ManagedJSON.class).in(env).and(JSON_READER).build();

		String json = "{ " +
		"			\"p1\" : \"hallo\", " +
		"			\"p2\" : {\"attr\" : \"whole wide\",\""+NONAME+"\" : \"world\"}," +
		"			\"p3\" : { \"p1\" : 10 }," +
		"			\"p4\" : [  \"hello\", \"world\" ]," +
		"			\"p5\" : [  \"hello\", \"world\" ]," +
		"			\"p6\" : { \"p1\" : \"hello\" } " +			
		"		}";

		
		ManagedJSON m = binder.bind(new StringReader(json));
		assertNotNull(m);
	}
}
