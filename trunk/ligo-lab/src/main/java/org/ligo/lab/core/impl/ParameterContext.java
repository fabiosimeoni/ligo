package org.ligo.lab.core.impl;

import static java.lang.String.*;
import static org.ligo.lab.core.keys.Keys.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Qualifier;

import org.ligo.lab.core.annotations.Bind;
import org.ligo.lab.core.annotations.BindingAnnotation;
import org.ligo.lab.core.keys.Key;


public class ParameterContext {
	
	private static final String BINDMETHOD_ERROR= 
		"@Bind is allowed only on single-type constructors/methods, this is not the case for %1s, annotate individual parameters instead";
	
	private final Member member;
	private final Type type;
	private final Annotation[] annotations;
	private Annotation bindingAnnotation;
	private Qualifier qualifier;
	
	public ParameterContext(Member m, Type p, Annotation[] as) {
		member=m; type=p; annotations=as;
		for (Annotation a : as)
			if (a.annotationType().isAnnotationPresent(BindingAnnotation.class)) {
				if (bindingAnnotation==null)
					bindingAnnotation= a;
				else
					throw new RuntimeException(format("mulitple binding annotations on %1s",member));
			}
			else
				if (a instanceof Qualifier)
					if (qualifier ==null)
						qualifier = (Qualifier) a;
					else
						throw new RuntimeException(format("mulitple qualifiers on %1s",member));
	}
	
	public boolean isBound() {
		return bindingAnnotation!=null;
	}
	
	public Key<?> key() {
		return qualifier==null?newKey(type):newKey(type,qualifier.annotationType());
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
	 * @return the type
	 */
	public Type type() {
		return type;
	}
	
	public static ParameterContext[] buildContexts(Constructor<?> c) {
		
		Type[] params = c.getGenericParameterTypes();
		
		//@Bind on constructor or @Bind on params?
		Bind bindAnnotation = c.getAnnotation(Bind.class);
		
		//@Bind on parameters 
		if (bindAnnotation==null) 
			return buildContextsForMember(c, params, c.getParameterAnnotations());
		
		//@Bind on constructor
		if (params.length!=1) //validate
			throw new RuntimeException(format(BINDMETHOD_ERROR,c));
		else
			return new ParameterContext[]{new ParameterContext(c,params[0],c.getAnnotations())};
	}
	
	static ParameterContext[] buildContexts(Method m) {
		return buildContexts(m,m.getGenericParameterTypes(),m.getParameterAnnotations());
	}
	
	static ParameterContext[] buildContexts(Method m,Type[] types, Annotation[][] as) {
		
		//@Bind on constructor or @Bind on params?
		Bind bindAnnotation = m.getAnnotation(Bind.class);
		
		//@Bind on parameters 
		if (bindAnnotation==null) 
			return buildContextsForMember(m, types, as);
		
		//@Bind on constructor
		if (types.length!=1) //validate
			throw new RuntimeException(format(BINDMETHOD_ERROR,m));
		else
			return new ParameterContext[]{new ParameterContext(m,types[0],m.getAnnotations())};
	}
	
	private static ParameterContext[] buildContextsForMember(Member m, Type[] types, Annotation[][] as) {
		
		List<ParameterContext> contexts = new ArrayList<ParameterContext>();
		for (int i=0; i<types.length;i++)
			contexts.add(new ParameterContext(m, types[i], as[i]));
		return contexts.toArray(new ParameterContext[0]);
	}
}