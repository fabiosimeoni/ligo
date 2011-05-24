package org.ligo.lab.typebinders.impl;

import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.TypeBinder;
import org.ligo.lab.typebinders.Environment;

public interface BinderProvider<T> {
	
	TypeBinder<T> binder(Key<T> key, Environment factory);
}