/**
 * 
 */
package org.ligo.nodes.patterns;

import static org.junit.Assert.*;
import static org.ligo.nodes.model.impl.Nodes.*;
import static org.ligo.nodes.patterns.Patterns.*;

import org.junit.Test;
import org.ligo.nodes.model.api.Node;

/**
 * @author Fabio Simeoni
 *
 */
public class PatternTests {

	@Test
	public void simple() {
		
		Node n = n(
					e("a",3),
					e("b",n(
							e("c","hello"),
							e("extra","extra")
						 )),
					e("extra","extra"));
		
		NodePattern pattern = node(
				one("a",any),
				one("b",node(
						one("c",string)))); 
		
		Node match = pattern.bind(n);
		
		Node expected = n(
						 e("a",3),
						 e("b",n(
								 e("c","hello"))));
		
		assertEquals(expected,match);
		
	}
}
