package org.ligo.lab.core.impl;

import java.lang.annotation.Annotation;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.Key;
import org.ligo.lab.core.TypeBinder;

public interface BinderProvider<T> {
	
	Key<? extends T> matchingKey();
	TypeBinder<T> binder(Class<T> clazz, Class<? extends Annotation> qualifier, Environment env);
}