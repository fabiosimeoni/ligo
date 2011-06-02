/**
 * 
 */
package org.ligo.core;

import static org.junit.Assert.*;
import static org.ligo.core.binders.BindMode.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ligo.core.TestClassDefs.Adapted2.Person;
import org.ligo.core.annotations.AbstractBindAdapter;
import org.ligo.core.annotations.Bind;
import org.ligo.core.annotations.BindAdapter;
import org.ligo.core.annotations.BindConstant;

/**
 * @author Fabio Simeoni
 *
 */
public class TestClassDefs {

	interface SomeInterface {}

	static class Empty implements SomeInterface {}
	
	static class BadPlacement implements SomeInterface {
		
		@Bind("a")
		public BadPlacement() {}
	}
	
	static class TooManyConstructors implements SomeInterface {
		
		@Bind("a")
		public TooManyConstructors(Integer i) {}
		
		@Bind("b")
		public TooManyConstructors(String s) {}
	}

	static class BindOnConstructor implements SomeInterface {
		
		@Bind(value="a")
		public BindOnConstructor(String s) {}
	}
	
	static class BindOnMethod implements SomeInterface {
		
		boolean invoked;
		
		@Bind(value="a")
		public void m(String s) {invoked=true;}
	}
	
	static class BindOnParam implements SomeInterface {
		
		boolean invoked;
		
		public void m(@Bind("a") String s) {
			invoked=true;
		}
	}
	
	static class BindOnManyParams implements SomeInterface {
		
		boolean invoked;
		
		public void m(@Bind("a") String s1, @Bind("b") String s2) {
			invoked=true;
		}
	}
	
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

	static class MultiParams implements SomeInterface {
		
		boolean invoked;
		
		public void m(@Bind("a") String s1, @Bind("b") String s2) {invoked=true;}
	}
	
	static class DuplicateLabels implements SomeInterface {
		
		@Bind("b")
		public DuplicateLabels(int s) {}
		
		public void m(@Bind("a") String s1, @Bind("b") String s2) {}
	}
	
	static class Partial implements SomeInterface {
		
		boolean invoked;
		
		public void m(@Bind("a") String s1, @BindConstant String s2, @BindConstant int s3, @Bind("b") String s4) {
			assertNull(s2);
			assertEquals(0,s3);
			invoked=true;
		}
	}
	
	static class Lax1 implements SomeInterface {
		
		boolean invoked1;
		boolean invoked2;
	
		@Bind(value="a",mode=LAX)
		public void m(String s) {
			assertNull(s);
			invoked1=true;
		}
		
		@Bind(value="b",mode=LAX)
		public void m(int s) {
			assertEquals(0,s);
			invoked2=true;
		}

	}
	
	static class Lax2 implements SomeInterface {
		
		boolean invoked;
		
		@Bind(value="a",mode=LAX)
		public void m(Inner s) {
			assertNull(s);
			invoked=true;
		}

		static class Inner {}
	}
	
	static interface BindingInterface {
		
		@Bind("a")
		void m(String s);
	}
	
	static class InterfaceImpl1 implements BindingInterface {
		
		boolean invoked;
		public void m(String s) {
			invoked=true;
		};
	}
	
	static class InterfaceImpl2 implements BindingInterface {
		
		boolean invoked;
		
		@Bind("b")
		public void m(String s) {
			invoked=true;
		};
	}
	
	
	static class Adapted {
		
		boolean invoked;
		
		@BindAdapter(value="a",adapter=MyAdapter.class)
		public void m(Integer i) {
			System.out.println(i);
			invoked=true;
		};
	}
	
	static class MyAdapter extends AbstractBindAdapter<String,Integer> {
		
		/**{@inheritDoc}*/
		@Override
		public Integer bind(String i) {
			return i.length();
		}
	}
	
	static class Adapted2 {
		
		boolean invoked;
		
		@BindAdapter(value="person",adapter=MyAdapter2.class)
		public void m(Map<String,Person> map) {
			System.out.println("invoked with "+map);
			invoked=true;
		};
		
		static class Person {
			
			String name;
			int age;
			
			@Bind("name")
			public void setName(String name) {
				this.name = name;
			}
			
			/**
			 * @return the name
			 */
			public String getName() {
				return name;
			}
			
			@Bind("age")
			public void setAge(int age) {
				this.age = age;
			}
			
			/**{@inheritDoc}*/
			@Override
			public String toString() {
				return name+"("+age+")";
			}
			
		}
	}
	
	static class MyAdapter2 extends AbstractBindAdapter<List<Person>,Map<String,Person>> {
		
		/**{@inheritDoc}*/
		@Override
		public Map<String, Person> bind(List<Person> values) {
			Map<String,Person> map = new HashMap<String, Person>();
			for (Person v: values)
				map.put(v.getName(), v);
			return map;
		}
	}
}
