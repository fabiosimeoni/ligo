package org.ligo.lab.core.impl;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.keys.ClassKey;

public interface BinderProvider<T> {
	
	ClassKey<? extends T> matchingKey();
	TypeBinder<T> binder(ClassKey<T> key, Environment env);
}