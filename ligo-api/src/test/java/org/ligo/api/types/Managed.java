package org.ligo.api.types;

import java.util.List;

import org.ligo.api.Project;

public class Managed {
	
	public Managed(@Project("p1") String s){
		System.out.println("invoked with "+s);
	}
	
	public void setString(@Project("p2") String s) {
		System.out.println("invoked with "+s);
	}
	
	public void setDep(@Project("p3") ManagedDep d) {
		System.out.println("invoked with "+d);
	}
	
	public void setList(List<String> d){
		
	}
}