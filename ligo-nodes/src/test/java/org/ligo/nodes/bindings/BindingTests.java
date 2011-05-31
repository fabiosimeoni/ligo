/**
 * 
 */
package org.ligo.nodes.bindings;

import static junit.framework.Assert.*;
import static org.ligo.dsl.Ligo.*;
import static org.ligo.nodes.binders.XMLBinders.*;
import static org.ligo.nodes.model.impl.Nodes.*;
import static org.ligo.nodes.binders.JSonBinders.*;

import java.io.Reader;
import java.io.StringReader;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.ligo.binders.Binder;
import org.ligo.core.data.Provided;
import org.ligo.core.impl.LigoEnvironment;
import org.ligo.core.impl.LigoResolver;
import org.ligo.nodes.model.api.Node;

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
		
		Binder<Provided,Managed> binder = bind(Managed.class).with(env).build();;
		
		Node n = n(
				e("p1","hello"),
				e("p2","world"),
				e("p3",n(e("p1",10))),
				e("p4","hello"),
				e("p4","world"),
				e("p5","hello"),
				e("p5","world"),
				e("p6",n(e("p1","hello")))
		);
		
		Managed m = binder.bind(n);
		assertNotNull(m);
	}
	
	@Test
	public void xml() throws Exception {
		
		resolver.bind(ManagedDep.class, DepImpl.class);
		
		Binder<Reader,Managed> binder = bind(Managed.class).with(env).and(XMLREADER_BINDER).build();
		
		String xml = "<doc>" +
				"		<p1>hello</p1>" +
				"		<p2>world</p2>" +
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
		
		Managed m = binder.bind(new StringReader(xml));
		assertNotNull(m);
	}
	
	@Test
	public void json() throws Exception {
		
		resolver.bind(ManagedDep.class, DepImpl.class);
		
		Binder<JSONObject,Managed> binder = bind(Managed.class).with(env).and(JSON_BINDER).build();

		String json = "{ " +
		"			p1 : hallo, " +
		"			p2 : world, " +
		"			p3 : { p1 : 10 }," +
		"			p4 : [  hello, world ]," +
		"			p5 : [  hello, world ]," +
		"			p6 : { p1 : hello } " +			
		"		}";

		
		Managed m = binder.bind(new JSONObject(json));
		assertNotNull(m);		
	}
		
}
