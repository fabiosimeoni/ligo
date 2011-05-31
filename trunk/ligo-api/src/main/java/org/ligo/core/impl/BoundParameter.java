package org.ligo.core.impl;

import static java.lang.String.*;
import static org.ligo.core.keys.Keys.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;

import javax.inject.Qualifier;

import org.ligo.core.annotations.BindingAnnotation;
import org.ligo.core.keys.Key;
import org.ligo.core.kinds.Kind;


public class BoundParameter<M extends Member> {
	
	private static final String MULITPLE_QUALIFIERS_ERROR = "mulitple qualifiers on %1s";
	private static final String MULITPLE_BINDING_ANNOTATIONS_ERROR = "mulitple binding annotations on %1s";
	
	private final M member;
	private final Kind<?> kind;
	private final Annotation[] annotations;
	private Annotation bindingAnnotation;
	private Qualifier qualifier;
	
	public BoundParameter(M m, Kind<?> k, Annotation[] as) {
		
		member=m; 
		kind=k; 
		annotations=as;
		
		for (Annotation a : as)
			if (a.annotationType().isAnnotationPresent(BindingAnnotation.class)) {
				if (bindingAnnotation==null)
					bindingAnnotation= a;
				else
					throw new RuntimeException(format(MULITPLE_BINDING_ANNOTATIONS_ERROR,member));
			}
			else
				if (a instanceof Qualifier)
					if (qualifier ==null)
						qualifier = (Qualifier) a;
					else
						throw new RuntimeException(format(MULITPLE_QUALIFIERS_ERROR,member));
	}
	
	public boolean isBound() {
		return bindingAnnotation!=null;
	}
	
	public Key<?> key() {
		return qualifier==null?newKey(kind):newKey(kind,qualifier.annotationType());
	}

	/**
	 * @return the qualifier
	 */
	public Qualifier qualifier() {
		return qualifier;
	}
	
	/**
	 * @return the bindingAnnotation
	 */
	public Annotation bindingAnnotation() {
		return bindingAnnotation;
	}
	
	/**
	 * @return the member
	 */
	public Member member() {
		return member;
	}
	
	/**
	 * @return the annotations
	 */
	public Annotation[] annotations() {
		return annotations;
	}
	
	/**
	 * @return the kind
	 */
	public Kind<?> kind() {
		return kind;
	}
}