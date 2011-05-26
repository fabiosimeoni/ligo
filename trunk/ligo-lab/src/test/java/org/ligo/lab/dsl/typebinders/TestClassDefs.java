/**
 * 
 */
package org.ligo.lab.dsl.typebinders;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.ligo.lab.typebinders.Bind;

/**
 * @author Fabio Simeoni
 *
 */
public class TestClassDefs {

	interface SomeInterface {}

	//un-annotated class
	static class Empty implements SomeInterface {}
	
	//standard class
	static class BadPlacement implements SomeInterface {
		
		@Bind("a")
		public BadPlacement() {}
	}
	
	//standard class
	static class TooManyConstructors implements SomeInterface {
		
		@Bind("a")
		public TooManyConstructors(Integer i) {}
		
		@Bind("b")
		public TooManyConstructors(String s) {}
	}
	
	//standard class
	static class BindOnMethod implements SomeInterface {
		
		boolean invoked;
		
		@Bind("a")
		public void m(String s) {invoked=true;}
	}
	
	//standard class
	static class BindOnParam implements SomeInterface {
		
		boolean invoked;
		
		public void m(@Bind("a") String s) {
			invoked=true;
		}
	}
	
	//standard class
	static class BindOnManyParams implements SomeInterface {
		
		boolean invoked;
		
		public void m(@Bind("a") String s1, @Bind("b") String s2) {
			invoked=true;
		}
	}
	
	//standard class
	static class Primitive implements SomeInterface {
		
		List<Boolean> invoked = new ArrayList<Boolean>();
		
		@Bind("1")
		public void m(int s) {invoked.add(true);}
		
		@Bind("2")
		public void m(long s) {invoked.add(true);}
		
		@Bind("3")
		public void m(byte s) {invoked.add(true);}
		
		@Bind("4")
		public void m(short s) {invoked.add(true);}
		
		@Bind("5")
		public void m(float s) {invoked.add(true);}
		
		@Bind("6")
		public void m(double s) {invoked.add(true);}
		
		@Bind("7")
		public void m(char s) {invoked.add(true);}
		
		@Bind("8")
		public void m(boolean s) {invoked.add(true);}
	}

	//standard class
	static class MultiParams implements SomeInterface {
		
		boolean invoked;
		
		public void m(@Bind("a") String s1, @Bind("b") String s2) {invoked=true;}
	}
	
	//standard class
	static class Partial implements SomeInterface {
		
		boolean invoked;
		
		public void m(@Bind("a") String s1, String s2, @Bind("c") String s3) {
			assertNull(s2);
			invoked=true;
		}
	}
	
	//standard class
	static class BadPartial implements SomeInterface {
		
		boolean invoked;
		
		public void m(@Bind("a") String s1, int s2, boolean s3, float s4, byte s5, short s6, char s7, Partial p, @Bind("c") String slast) {
			assertFalse(s3);
			assertEquals(0,s2);
			invoked=true;
		}
	}
}
