/**
 * 
 */
package org.ligo.core.binders.impl;

import static java.lang.String.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ligo.core.annotations.BindingAnnotation;
import org.ligo.core.binders.Environment;
import org.ligo.core.keys.Key;
import org.ligo.data.LigoObject;

/**
 * @author Fabio Simeoni
 *
 */
public class MethodBinder extends MemberBinder<Method> {
	
	public MethodBinder(Key<?> key, Method m, Environment env) {
		super(key,env);
		
		setMember(m);
		
		setParameterBinders(getParameters(m));
		
		//scan interfaces for possible annotations
		if (parameterBinders().isEmpty())
			for (Class<?> i : m.getDeclaringClass().getInterfaces())
				try {
					//find methods by 'raw' type (interface could be parametric)
					Method overridden = i.getMethod(m.getName(),m.getParameterTypes());
					//but do use the resolved parameters
					setParameterBinders(getParameters(overridden,m.getGenericParameterTypes(), overridden.getParameterAnnotations()));
				}
				catch(NoSuchMethodException e) {
					continue;
				}

		m.setAccessible(true);
	}

	
	List<BoundParameter<Method>> getParameters(Method m) {
		return getParameters(m,m.getGenericParameterTypes(),m.getParameterAnnotations());
	}
	
	List<BoundParameter<Method>> getParameters(Method m,Type[] types, Annotation[][] as) {
		
		//annotation on constructor or on params?
		for (Annotation a : m.getAnnotations())
			if (a.annotationType().isAnnotationPresent(BindingAnnotation.class))
				if (types.length!=1) //validate
					throw new RuntimeException(format(INVALID_BIND_ONMETHOD_ERROR,m));
				else
					return super.getParameters(m,new Type[]{types[0]},new Annotation[][]{m.getAnnotations()});
		

		//annotation on constructor
		return super.getParameters(m, types, as);
		
	}
	
	static List<MethodBinder> getMethodBinders(Key<?> key, Environment env) {
		
		Class<?> clazz = key.toClass();
		
		List<MethodBinder> boundMethods = new ArrayList<MethodBinder>();
		Map<String,Type[]> visitedMethods = new HashMap<String,Type[]>();
		
		do 
		
			for (Method m : clazz.getDeclaredMethods()) {
				
				//exclude overridden method
				if (Arrays.equals(m.getParameterTypes(),visitedMethods.get(m.getName())))
					continue;
				
				//mark visited method with its raw parameter types so as to detect synthetic overrides of generic types.
				visitedMethods.put(m.getName(),m.getParameterTypes());
			
				
				MethodBinder boundMethod = new MethodBinder(key,m, env);
				
				if (!boundMethod.parameterBinders().isEmpty())
					boundMethods.add(boundMethod);
			}
		
		while //repeat for inherited methods
			((clazz=clazz.getSuperclass())!=Object.class);
		
		return boundMethods;
		
	}
	
	public void bind(Object javaObject, LigoObject ligoObject) throws Exception {
		
		List<Object> values = new ArrayList<Object>();
		
		for (ParameterBinder<?> pbinder : parameterBinders())
			values.add(pbinder.bind(ligoObject));
		
		member().invoke(javaObject,values.toArray(new Object[0]));

	}
}
