/**
 * 
 */
package org.ligo.lab.dsl.typebinders;

import static org.junit.Assert.*;
import static org.ligo.lab.typebinders.Key.*;
import static org.ligo.lab.typebinders.kinds.Kind.*;
import static org.ligo.lab.typebinders.kinds.Kind.KindValue.*;

import java.util.List;

import org.junit.Test;
import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.Literal;
import org.ligo.lab.typebinders.kinds.Kind;

/**
 * @author Fabio Simeoni
 *
 */
public class KeyAndKindTest {

	Literal<String> lit = new Literal<String>() {};
	Literal<List<String>> plit = new Literal<List<String>>() {};
	Literal<List<String>[]> palit = new Literal<List<String>[]>() {};
	
	
	@Test
	public void literals() {
		
		//non-parametric type (pathological case)
		assertEquals(String.class, lit.type());
		assertEquals(new Literal<String>() {}, lit);
		assertEquals(CLASS,kindOf(lit.type()).value());
		
		//parametric type (key case)
		assertEquals(new Literal<List<String>>() {}, plit);
		assertEquals(GENERIC,kindOf(plit.type()).value());;
		
		//parametric array (key case)
		assertEquals(new Literal<List<String>[]>() {}, palit);
		assertEquals(GENERICARRAY,kindOf(palit.type()).value());
		
	}
	
	@Test
	public void kinds() {
		
		Kind<?> kind = kindOf(String.class);
		assertEquals(kindOf(String.class), kind);
		assertEquals(CLASS, kind.value());
		assertEquals(String.class, CLASS(kind));
		
		kind = kindOf(plit.type());
		assertEquals(GENERIC, kind.value());
		
		kind = kindOf(palit.type());
		assertEquals(GENERICARRAY, kind.value());

	}
	
	@Test
	public void keys() {
		
		Key<String> skey = get(String.class);
		Kind<?> kind = skey.kind();
		assertEquals(CLASS, kind.value());
		assertEquals(String.class, CLASS(kind));
		
		Literal<List<String>> lit = new Literal<List<String>>() {};
		Key<List<String>> pkey = get(lit);
		kind = pkey.kind();
		assertEquals(GENERIC, kind.value());
		assertEquals(lit.type(), GENERIC(kind));
	}
	
	static class SomeClass {}
}
