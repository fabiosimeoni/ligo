/**
 * 
 */
package org.ligo.core;

import static org.junit.Assert.*;
import static org.ligo.data.impl.DataBuilders.*;

import org.junit.Test;
import org.ligo.core.TestGenericsClassDefs.Generic;
import org.ligo.core.TestGenericsClassDefs.GenericField;
import org.ligo.core.TestGenericsClassDefs.IndirectNestedGeneric;
import org.ligo.core.TestGenericsClassDefs.MyAnnotatedGeneric;
import org.ligo.core.TestGenericsClassDefs.MyGeneric;
import org.ligo.core.TestGenericsClassDefs.NestedGeneric;
import org.ligo.core.TestGenericsClassDefs.Sub;
import org.ligo.core.impl.LigoEnvironment;
import org.ligo.core.impl.LigoResolver;

/**
 * @author Fabio Simeoni
 *
 */
public class BinderGenericsTests {

	LigoResolver resolver = new LigoResolver();
	Environment env = new LigoEnvironment(resolver);

	
	@Test
	public void generic() {
	
		//standard, flat-generic
		TypeBinder<Generic<String>> b = env.binderFor(new Literal<Generic<String>>() {});
		
		Generic<String> r = b.bind(o(n("b","hello")));
		
		assertNotNull(r);
		assertNotNull(r.t);
	}
	
	@Test
	public void genericOfgeneric() {
	
		//generic-of-a-generic: variable bindings retract along call chain
		TypeBinder<Generic<Generic<String>>> b = env.binderFor(new Literal<Generic<Generic<String>>>() {});
		
		Generic<Generic<String>> r = b.bind(o(
												n("b",
													o(n("b","hello")))));
		assertNotNull(r);
		assertNotNull(r.t);
		assertNotNull(r.t.t);
	}
	
	@Test
	public void genericField() {
		
		//fields with different instantiations of same generic: Generic<String>, Generic<Integer>
		TypeBinder<GenericField> b = env.binderFor(GenericField.class);	
		
		GenericField r = b.bind(o(
								n("a",o(n("b","hello"))),
								n("b",o(n("b",5)))));
		assertNotNull(r);
		assertNotNull(r.gs);
		assertNotNull(r.gi);

	}
	
	@Test
	public void nestedGeneric() {
	
		//generic inherits binding from outer one: NestedGeneric<T> contains Generic<T>
		TypeBinder<NestedGeneric<String>> b = env.binderFor(new Literal<NestedGeneric<String>>() {});
		
		NestedGeneric<String> r = b.bind(o(n("b",o(n("b","hello"))),n("c","world")));
		
		assertNotNull(r);
		assertNotNull(r.t);
		assertNotNull(r.gt);
		assertNotNull(r.gt.t);
	}
	
	@Test
	public void indirectNestedGeneric() {
	
		//Generic<Dep> where Dep contains Generic<Integer>
		TypeBinder<IndirectNestedGeneric> b = env.binderFor(new Literal<IndirectNestedGeneric>() {});
		
		IndirectNestedGeneric r = b.bind(o(
											n("a",
												o(n("b",
													o(n("a",
														o(n("b",5)))))))));
		
		assertNotNull(r);
		assertNotNull(r.gd);
		assertNotNull(r.gd.t);
		assertNotNull(r.gd.t.gi);
	}
	
	
	
	@Test
	public void subclass() {
		
		//Non generic class obtained by instantiation of generic one: MyGenerics extends Generic<String>
		// variable bindings go up the inheritance hierarchy so that we can process inherited methods 
		TypeBinder<MyGeneric> b = env.binderFor(MyGeneric.class);	
		
		MyGeneric r = b.bind(o(n("b","hello"),n("c",3)));
		
		assertNotNull(r);
		assertNotNull(r.t);
		assertNotNull(r.i);
	}
	
	@Test
	public void annotatedSubclass() {
		
		//As before, but this this with overriding
		TypeBinder<MyAnnotatedGeneric> b = env.binderFor(MyAnnotatedGeneric.class);	
		
		MyAnnotatedGeneric r = b.bind(o(n("a","hello"),n("c",3)));
		
		assertNotNull(r);
		assertNotNull(r.t);
		assertNotNull(r.i);
	}
	
	@Test
	public void inheritance() {
		
		//As before, but this this with overriding
		TypeBinder<Sub<Integer>> b = env.binderFor(new Literal<Sub<Integer>>() {});	
		
		Sub<Integer> r = b.bind(o(n("a","hello"),n("b",3)));
		
		assertNotNull(r);
	}
}

