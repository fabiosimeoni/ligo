package org.ligo.lab.core.impl;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.keys.ClassKey;
import org.ligo.lab.core.keys.Key;

public interface BinderProvider<T> {
	
	ClassKey<? extends T> matchingKey();
	TypeBinder<T> binder(ClassKey<T> classKey, Key<T> key, Environment env);
}