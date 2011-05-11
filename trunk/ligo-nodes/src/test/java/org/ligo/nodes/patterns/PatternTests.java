/**
 * 
 */
package org.ligo.nodes.patterns;

import static org.ligo.nodes.model.impl.Nodes.*;
import static org.ligo.nodes.patterns.Patterns.*;

import org.junit.Test;
import org.ligo.nodes.patterns.NodePattern;

/**
 * @author Fabio Simeoni
 *
 */
public class PatternTests {

	@Test
	public void simple() {
		
		NodePattern pattern = node(
				one("a",any),
				one("b",string),
				one(lbl("http://myns","c"),integer)); 
		
		System.out.println(pattern);
		
	}
}
