package org.ligo.lab.core.impl;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.keys.Key;

public interface BinderProvider<T> {
	
	Key<? extends T> matchingKey();
	TypeBinder<T> binder(Key<T> key, Environment env);
}