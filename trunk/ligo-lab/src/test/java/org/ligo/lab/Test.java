/**
 * 
 */
package org.ligo.lab;


/**
 * @author Fabio Simeoni
 *
 */
public class Test {

	static class A<T> {
		
		void inherited() {}
		void overridden() {}
		void overridden(String s) {}
	}
	
	static class B<T> extends A<T> {
	
		void overridden() {}
		void own() {}
	}

	public static void main(String[] args) {

		System.out.println(String[].class.isArray());
		
	}
}
