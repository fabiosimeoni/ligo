package org.ligo.json;

import java.util.List;

import org.ligo.core.annotations.Bind;
import org.ligo.json.Dependency;

public class Managed {
	
	final String p1;
	Dependency p2;
	List<String> p3;
	List<Dependency> p4;
	List<List<String>> p5;
	List<List<Dependency>> p6;
	List<Generic<Dependency>> p7;
	
	@Bind("p1")
	Managed(String s){
		p1=s;
	}
	
	@Bind("p2")
	void setDependency(Dependency d) {
		p2=d;
	}
	
	@Bind("p3")
	void setScalarList(List<String> l){
		p3=l;
	}
	
	@Bind("p4")
	void setDependencyList(List<Dependency> l){
		p4=l;
	}
	
	@Bind("p5")
	public void setNestedScalarList(List<List<String>> s){
		p5=s;
	}
	
	@Bind("p6")
	public void setNestedDependencyList(List<List<Dependency>> s){
		p6=s;
	}
	
	@Bind("p7") 
	public void setParametricDep(List<Generic<Dependency>> s){
		p7=s;
	}
}