package org.ligo.nodes.bindings;

class DepImpl implements ManagedDep {
	
	public void setInteger(Integer i){
		System.out.println("invoked with "+i);
	}
}