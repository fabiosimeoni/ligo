package org.ligo.core.annotations;

import static org.ligo.core.keys.Keys.*;
import static org.ligo.core.kinds.Kind.*;

import org.ligo.core.keys.Key;

@SuppressWarnings("unchecked")
public abstract class AbstractBindAdapter<INTYPE,OUTTYPE> {
	
	public abstract OUTTYPE bind(INTYPE i);
	
	
	public Key<INTYPE> inKey() {
		return (Key) newKey(GENERIC(kindOf(getClass().getGenericSuperclass())).getActualTypeArguments()[0]);
	}
	
	public Key<OUTTYPE> outKey() {
		return (Key) newKey(GENERIC(kindOf(getClass().getGenericSuperclass())).getActualTypeArguments()[1]);
	}
}