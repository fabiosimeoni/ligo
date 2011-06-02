/**
 * 
 */
package org.ligo.core.binders.impl;

import static java.lang.String.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.ligo.core.annotations.BindingAnnotation;
import org.ligo.core.binders.api.Environment;
import org.ligo.core.keys.Key;
import org.ligo.data.LigoObject;

/**
 * @author Fabio Simeoni
 *
 */
public class ConstructorBinder extends MemberBinder<Constructor<?>> {

	static final String MULTICONSTRUCTOR_ERROR= "%1s has more than one bound constructor";
	static final String DUPLICATE_NAME= "bound name '%1s' is duplicated in %2s";
	static final String NO_CONSTRUCTOR_ERROR="%1s has no nullary or annotated constructors";
	
	public ConstructorBinder(Key<?> key, Environment env) {
		
		super(key,env);
		
		//there must be only one annotated constructor
		//if this takes one parameter, then annotations can go on the constructor itself.
		//if it takes more, then annotations must go on parameters though not all parameters must have them.
		
		Constructor<?> constructor=null;
		
		Class<?> clazz = key.toClass();
		
		//identify constructor
		for (Constructor<?> c : clazz.getDeclaredConstructors()) {
			
			setParameterBinders(getParameters(c));
			
			//remember and check uniqueness
			if (parameterBinders().size()>0) {
				if (constructor==null)
					constructor=c;
				else
					throw new RuntimeException(format(MULTICONSTRUCTOR_ERROR,clazz.getName()));
			}	
		
		}
		
		//no constructor yet, use nullary one
		if (constructor==null)
			try {
				constructor = clazz.getDeclaredConstructor();
			}
			catch(Throwable e) {
				throw new RuntimeException(format(NO_CONSTRUCTOR_ERROR,clazz.getName()));
			}
		
		//prep later access
		constructor.setAccessible(true);
		
		setMember(constructor);
	}
	
	List<BoundParameter<Constructor<?>>> getParameters(Constructor<?> c) {
		
		Type[] params = c.getGenericParameterTypes();
		
		//annotation on constructor or on params?
		for (Annotation a : c.getAnnotations())
			if (a.annotationType().isAnnotationPresent(BindingAnnotation.class))
				if (params.length!=1) //validate
						throw new RuntimeException(format(INVALID_BIND_ONMETHOD_ERROR,c));
					else
						return getParameters(c,new Type[]{params[0]},new Annotation[][]{c.getAnnotations()});
		
		//annotation on constructor
		return getParameters(c, params, c.getParameterAnnotations());
		
	}
	
	public Object bind(LigoObject ligoObject) {
		
		List<Object> values = new ArrayList<Object>();
		
		for (ParameterBinder<?> pbinder : parameterBinders())
			values.add(pbinder.bind(ligoObject));
		
		return environment().resolver().resolve(key().toClass(),values);
		
		
	}
}
