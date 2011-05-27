/**
 * 
 */
package org.ligo.lab.core;

import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.lab.core.data.DataProvider;
import org.ligo.lab.core.data.Provided;
import org.ligo.lab.core.data.StructureProvider;
import org.ligo.lab.core.data.ValueProvider;


/**
 * @author Fabio Simeoni
 *
 */
public class TestData {

	

	
	public static List<Provided> s(final Pair ...ps) {
		
		return Collections.<Provided>singletonList(new Provided() {
			
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
		});
	}

	public static List<Provided> v(final Object o) {
		return Collections.<Provided>singletonList(new Provided() {
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
		});
		
	}
	
	public static Pair p(String s,List<Provided> p) {
		return new Pair(s,p.get(0));
	}
	public static Pair p(String s,Object o) {
		return new Pair(s,v(o).get(0));
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
