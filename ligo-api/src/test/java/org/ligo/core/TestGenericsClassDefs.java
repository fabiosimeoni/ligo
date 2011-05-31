/**
 * 
 */
package org.ligo.core;

import org.ligo.core.annotations.Bind;


/**
 * @author Fabio Simeoni
 *
 */
public class TestGenericsClassDefs {

	static class Generic<T> {
		T t;
		
		@Bind("b")
		void m(T s) {t=s;}
	}
	
	static class GenericField {
		Generic<String> gs;
		Generic<Integer> gi;
		
		@Bind("a")
		void m(Generic<String> s) {gs=s;}
		
		@Bind("b")
		void n(Generic<Integer> s) {gi=s;}

	}
	

	
	static class NestedGeneric<T> {
		Generic<T> gt;
		T t;
		
		@Bind("b")
		void m(Generic<T> s) {gt=s;}
		
		@Bind("c")
		void m(T s) {t=s;}
	}

	static class IndirectNestedGeneric {
		Generic<Dep> gd;
		
		@Bind("a")
		void p(Generic<Dep> s) {gd=s;}
	}
	
	static class Dep {
		Generic<Integer> gi;
		
		@Bind("a")
		void m(Generic<Integer> s) {gi=s;}
	}
	
	static class MyGenericSuperclass<T> {
		T t;
		Integer i;
		
		//used when inherited but ignored if overridden
		@Bind("b")
		void m(T s) {t=s;}
		
		@Bind("c")
		void m(Integer s) {i=s;}
	}
	
	static class MyGeneric extends MyGenericSuperclass<String> {
		//inherits m() with its annotations
	}
	
	static class MyAnnotatedGeneric extends MyGenericSuperclass<String> {
		
		@Bind("a")
		void m(String s) {super.m(s);}  //overrides 
	}
	
	
	static class Top<T> {
		
		@Bind("a")
		void m(T t) {}
	}
	
	static class Mid extends Top<String> {
		
	}
	
	static class Sub<S> extends Mid {
		
		@Bind("b")
		void n(S t) {}
	}
}
