/**
 * 
 */
package org.ligo.lab.core;

import org.ligo.lab.core.annotations.Bind;


/**
 * @author Fabio Simeoni
 *
 */
public class TestGenericsClassDefs {

	static class GenericField {
		
		@Bind("a")
		void m(GenericDep<String> s) {}
		
	}
	
	static class GenericDep<T> {
		
		@Bind("b")
		void m(T s) {}
	}
}
