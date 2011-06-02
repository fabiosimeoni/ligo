package org.ligo.nodes.bindings;

import java.util.List;
import java.util.Set;

import org.ligo.core.annotations.Bind;

public class ManagedJSON {
	
	static class Structure {
		
		public void set(@Bind("attr") String s, @Bind String value) {
			System.out.println("invoked with "+s+" and "+value);
		}
	}
	public ManagedJSON(@Bind("p1") String s){
		System.out.println("invoked with "+s);
	}
	
	public void setString(@Bind("p2") Structure s) {
		System.out.println("invoked with "+s);
	}
	
	public void setDep(@Bind("p3") ManagedDep d) {
		System.out.println("invoked with "+d);
	}
	
	@Bind("p4/(.*)")
	public void setList(List<String> l){
		System.out.println("p4 invoked with "+l);
	}
	
	@Bind("p5/(.*)")
	public void setList(Set<String> s){
		System.out.println("p5 invoked with "+s);
	}
	
	public void setParametricDep(@Bind("p6") GenericType<String> s){
		System.out.println("invoked with "+s);
	}
}