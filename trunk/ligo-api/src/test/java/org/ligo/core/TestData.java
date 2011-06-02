/**
 * 
 */
package org.ligo.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.ligo.core.data.LigoData;
import org.ligo.core.data.LigoObject;
import org.ligo.core.data.LigoValue;
import org.ligo.core.data.impl.AbstractLigoObject;
import org.ligo.core.data.impl.AbstractLigoValue;


/**
 * @author Fabio Simeoni
 *
 */
public class TestData {

	
	public static List<LigoData> list(LigoData ... data) {
		
		List<LigoData> result = new LinkedList<LigoData>();
		for (LigoData d : data)
			result.add(d);
		return result;
		
	}
	
	public static LigoObject s(final Pair ...ps) {
		
		return new AbstractLigoObject() {
					public List<LigoData> get(QName name) {
						List<LigoData> result = new ArrayList<LigoData>();
						for (Pair p : ps)
							if (p.name.equals(name))
								result.add(p.data);
						return result;
					}
					public Set<QName> names() {
						Set<QName> names = new HashSet<QName>();
						for (Pair p : ps)
							names.add(p.name);
						return names;
					}
				};
	}

	public static LigoValue v(final Object o) {
		return new AbstractLigoValue() {
					public Object get() {return o;}
				};
	}
	
	public static Pair p(String s,LigoData p) {
		return new Pair(new QName(s),p);
	}
	public static Pair p(String s,Object o) {
		return new Pair(new QName(s),v(o));
	}
	
	static class Pair {
		QName name; LigoData data;
		Pair(QName n, LigoData d) {
			name=n;
			data=d;
		}
		public String toString() {
			return name+":"+data;
		}
	}
	
	

}
