/**
 * 
 */
package org.ligo.core;

import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.core.data.DataProvider;
import org.ligo.core.data.Provided;
import org.ligo.core.data.StructureProvider;
import org.ligo.core.data.ValueProvider;


/**
 * @author Fabio Simeoni
 *
 */
public class TestData {

	
	public static List<Provided> list(Provided ... ps) {
		
		List<Provided> result = new LinkedList<Provided>();
		for (Provided p : ps)
			result.add(p);
		return result;
		
	}
	
	public static Provided s(final Pair ...ps) {
		
		return new Provided() {
			
			public DataProvider provider() {
				return new StructureProvider() {
					public List<Provided> get(QName name) {
						List<Provided> result = new ArrayList<Provided>();
						for (Pair p : ps)
							if (p.s.equals(name.getLocalPart()))
								result.add(p.p);
						return result;
					}
					public String toString() {
						return asList(ps).toString();
					}
				};
			}
			public String toString() {
				return asList(ps).toString();
			}
		};
	}

	public static Provided v(final Object o) {
		return new Provided() {
			public DataProvider provider() {
				return new ValueProvider() {
					public Object get() {
						return o;
					}
					public String toString() {
						return o.toString();
					}
				};
				
			}

			public String toString() {
				return o.toString();
			}
		};
		
	}
	
	public static Pair p(String s,Provided p) {
		return new Pair(s,p);
	}
	public static Pair p(String s,Object o) {
		return new Pair(s,v(o));
	}
	
	static class Pair {
		String s; Provided p;
		Pair(String s, Provided p) {
			this.s=s;this.p=p;
		}
		public String toString() {
			return s+":"+p.toString();
		}
	}
}
