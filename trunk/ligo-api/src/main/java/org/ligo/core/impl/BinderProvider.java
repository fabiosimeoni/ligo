package org.ligo.core.impl;

import org.ligo.core.Environment;
import org.ligo.core.TypeBinder;
import org.ligo.core.keys.Key;

public interface BinderProvider<T> {
	
	Key<? extends T> matchingKey();
	TypeBinder<T> binder(Key<T> key, Environment env);
}