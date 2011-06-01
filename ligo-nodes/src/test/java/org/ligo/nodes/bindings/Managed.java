package org.ligo.nodes.bindings;

import java.util.List;
import java.util.Set;

import org.ligo.core.annotations.Bind;

public class Managed {
	
	static class Structure {
		
		public void set(@Bind("attr") String s, @Bind String value) {
			System.out.println("invoked with "+s+" and "+value);
		}
	}
	public Managed(@Bind("p1") String s){
		System.out.println("invoked with "+s);
	}
	
	public void setString(@Bind("p2") Structure s) {
		System.out.println("invoked with "+s);
	}
	
	public void setDep(@Bind("p3") ManagedDep d) {
		System.out.println("invoked with "+d);
	}
	
	public void setList(@Bind("p4") List<String> l){
		System.out.println("invoked with "+l);
	}
	
	public void setList(@Bind("p5") Set<String> s){
		System.out.println("invoked with "+s);
	}
	
	public void setParametricDep(@Bind("p6") GenericType<String> s){
		System.out.println("invoked with "+s);
	}
}