package org.ligo.lab.core.impl;

public abstract class BindingAdapter<INTYPE,OUTTYPE> {
	
	public abstract OUTTYPE bindIn(INTYPE i);
	public abstract INTYPE bindOut(OUTTYPE i);
}