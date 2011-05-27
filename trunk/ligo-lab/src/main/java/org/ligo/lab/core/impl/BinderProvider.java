package org.ligo.lab.core.impl;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.Key;
import org.ligo.lab.core.TypeBinder;

public interface BinderProvider<T> {
	
	Key<? extends T> matchingKey();
	TypeBinder<T> binder(Key<T> key, Environment factory);
}