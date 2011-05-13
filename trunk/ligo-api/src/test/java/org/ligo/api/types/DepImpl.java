package org.ligo.api.types;

class DepImpl implements ManagedDep {
	
	public void setInteger(Integer i){
		System.out.println("invoked with "+i);
	}
}