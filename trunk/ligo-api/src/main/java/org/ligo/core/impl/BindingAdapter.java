package org.ligo.core.impl;

import static org.ligo.core.kinds.Kind.*;

import org.ligo.core.kinds.Kind;

public abstract class BindingAdapter<INTYPE,OUTTYPE> {
	
	public abstract OUTTYPE bind(INTYPE i);
	
	public Kind<?> inKind() {
		return kindOf(GENERIC(kindOf(getClass().getGenericSuperclass())).getActualTypeArguments()[0]);
	}
	
	public Kind<?> outKind() {
		return kindOf(GENERIC(kindOf(getClass().getGenericSuperclass())).getActualTypeArguments()[1]);
	}
}