/**
 * 
 */
package org.ligo.core;

import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.ligo.core.data.LigoData;
import org.ligo.core.data.LigoObject;
import org.ligo.core.data.LigoProvider;
import org.ligo.core.data.LigoValue;


/**
 * @author Fabio Simeoni
 *
 */
public class TestData {

	
	public static List<LigoProvider> list(LigoProvider ... ps) {
		
		List<LigoProvider> result = new LinkedList<LigoProvider>();
		for (LigoProvider p : ps)
			result.add(p);
		return result;
		
	}
	
	public static LigoProvider s(final Pair ...ps) {
		
		return new LigoProvider() {
			
			public LigoData provide() {
				return new LigoObject() {
					public List<LigoProvider> get(QName name) {
						List<LigoProvider> result = new ArrayList<LigoProvider>();
						for (Pair p : ps)
							if (p.s.equals(name.getLocalPart()))
								result.add(p.p);
						return result;
					}
					public Set<QName> names() {
						Set<QName> names = new HashSet<QName>();
						for (Pair p : ps)
							names.add(new QName(p.s));
						return names;
					}
					public boolean equals(Object obj) {
						if (!(obj instanceof LigoObject))
							return false; 
						LigoObject lobj = (LigoObject) obj; 
						for (Pair p : ps)
							if (!(lobj.get(new QName(p.s)).equals(get(new QName(p.s)))))
							 return false;
						return true;
					}
					public String toString() {
						return asList(ps).toString();
					}
				};
			}
			/**{@inheritDoc}*/
			@Override
			public boolean equals(Object obj) {
				return obj instanceof LigoProvider && ((LigoProvider) obj).provide().equals(provide());
			}
			public String toString() {
				return asList(ps).toString();
			}
		};
	}

	public static LigoProvider v(final Object o) {
		return new LigoProvider() {
			public LigoData provide() {
				return new LigoValue() {
					public Object get() {
						return o;
					}
					/**{@inheritDoc}*/
					@Override
					public boolean equals(Object obj) {
						return obj instanceof LigoValue && ((LigoValue) obj).get().equals(o);
					}
					public String toString() {
						return o.toString();
					}
				};
				
			}
			/**{@inheritDoc}*/
			@Override
			public boolean equals(Object obj) {
				return obj instanceof LigoProvider && ((LigoProvider) obj).provide().equals(provide());
			}
			public String toString() {
				return o.toString();
			}
		};
		
	}
	
	public static Pair p(String s,LigoProvider p) {
		return new Pair(s,p);
	}
	public static Pair p(String s,Object o) {
		return new Pair(s,v(o));
	}
	
	static class Pair {
		String s; LigoProvider p;
		Pair(String s, LigoProvider p) {
			this.s=s;this.p=p;
		}
		public String toString() {
			return s+":"+p.toString();
		}
	}
	
	

}