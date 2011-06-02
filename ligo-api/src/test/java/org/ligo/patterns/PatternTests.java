/**
 * 
 */
package org.ligo.patterns;

import static org.junit.Assert.*;
import static org.ligo.data.impl.DataBuilders.*;
import static org.ligo.patterns.impl.Patterns.*;

import org.junit.Test;
import org.ligo.data.LigoData;
import org.ligo.data.LigoObject;
import org.ligo.patterns.api.LigoPattern;

/**
 * @author Fabio Simeoni
 *
 */
public class PatternTests {

	@Test
	public void simple() {
		
		LigoObject o = o(
						n("a",3),
						n("b",o(
								n("c","hello"),
								n("extra","extra"))),
						 n("extra","extra"));
		
		LigoPattern pattern = object(
								one("a",any),
								one("b",object(one("c",string)))); 
		
		LigoData match = pattern.bind(o);
		
		LigoObject expected = o(n("a",3),n("b",o(n("c","hello"))));
		
		assertEquals(expected,match);
		
	}
}
