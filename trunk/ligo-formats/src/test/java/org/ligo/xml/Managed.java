package org.ligo.xml;

import java.util.List;

import org.ligo.core.annotations.Bind;

class Managed {
	
	final String p1;
	Dependency p2;
	List<String> p3;
	List<Dependency> p4;
	List<List<String>> p5;
	List<List<Dependency>> p6;
	List<Generic<Dependency>> p7;
	Structure p8;
	
	@Bind("p1")
	Managed(String s){
		p1=s;
	}
	
	@Bind("p2")
	void setDependency(Dependency d) {
		p2=d;
	}
	
	@Bind("p3[]")
	void setScalarList(List<String> l){
		p3=l;
	}
	
	@Bind("p4[]")
	void setDependencyList(List<Dependency> l){
		p4=l;
	}
	
	@Bind("p5[el[el]]")
	public void setNestedScalarList(List<List<String>> s){
		p5=s;
	}
	
	@Bind("p6[[]]")
	public void setNestedDependencyList(List<List<Dependency>> s){
		p6=s;
	}
	
	@Bind("p7[]") 
	public void setParametricDep(List<Generic<Dependency>> s){
		p7=s;
	}
	
	@Bind("p8") 
	public void setAttributedLeaf(Structure s){
		p8=s;
	}
	
	static class Structure {
		String attr;
		String value;

		public Structure(@Bind("attr") String s, @Bind String v) {
			attr=s;
			value=v;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((attr == null) ? 0 : attr.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Structure))
				return false;
			Structure other = (Structure) obj;
			if (attr == null) {
				if (other.attr != null)
					return false;
			} else if (!attr.equals(other.attr))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
	}
}