package org.ligo.core.binders.impl;

import org.ligo.core.binders.Environment;
import org.ligo.core.binders.TypeBinder;
import org.ligo.core.keys.Key;

public interface BinderProvider<T> {
	
	Key<? extends T> matchingKey();
	TypeBinder<T> binder(Key<T> key, Environment env);
}