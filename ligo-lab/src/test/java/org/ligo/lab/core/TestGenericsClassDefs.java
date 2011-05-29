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
		boolean invokedm;
		boolean invokedn;
		@Bind("a")
		void m(Generic<String> s) {invokedm=true;}
		@Bind("b")
		void n(Generic<Integer> s) {invokedn=true;}
		@Bind("c")
		void p(Generic<Dep> s) {}
	}
	
	static class Dep {
		@Bind("a")
		void m(Generic<Integer> s) {}
	}
	
	static class Generic<T> {
		boolean invoked;
		@Bind("b")
		void m(T s) {invoked=true;}
	}
	
	static class NestedGeneric<T> {
		boolean invoked;
		@Bind("b")
		void m(Generic<T> s) {invoked=true;}
		
		@Bind("c")
		void m(T s) {invoked=true;}
	}
	
	static class MyGenericSuperclass<T> {
		boolean invoked;
		//used when inherited but ignored if overridden
		@Bind("b")
		void m(T s) {invoked=true;}
		@Bind("c")
		void m(Integer s) {invoked=true;}
	}
	
	static class MyGeneric extends MyGenericSuperclass<String> {}
	
	static class MyAnnotatedGeneric extends MyGenericSuperclass<String> {
		boolean invoked;
		@Bind("a")
		void m(String s) {invoked=true;}
	}
	
	public static void main(String[] args) {
		
		System.out.println(Generic.class.getTypeParameters()[0].equals(NestedGeneric.class.getTypeParameters()[0]));
	}
}
