/**
 * 
 */
package org.ligo.core.resolvers.impl;

import static java.lang.String.*;
import static org.ligo.core.keys.Keys.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ligo.core.annotations.Bind;
import org.ligo.core.keys.Key;
import org.ligo.core.keys.Literal;
import org.ligo.core.resolvers.api.ConfigurableResolver;

/**
 * @author Fabio Simeoni
 *
 */
public class LigoResolver implements ConfigurableResolver {

	private Map<Key<?>, List<Class<?>>> bindings = new HashMap<Key<?>, List<Class<?>>>();
	
	public LigoResolver() {
		bind(List.class,LinkedList.class);
		bind(Set.class,HashSet.class);
		bind(Iterator.class,Collections.emptyList().iterator().getClass());
	}
	
	/**{@inheritDoc}*/
	@SuppressWarnings("unchecked") //internally consistent
	public synchronized <T> List<Class<? extends T>> resolve(Key<T> key) {
	
		List<Class<? extends T>> bound = (List) bindings.get(key);
		
		if (bound==null)
			bound = (List) bindings.get(newKey(key.toClass(),key.qualifier()));
		
		if (bound==null) {
			List<Class<?>> singleton = new ArrayList<Class<?>>();
			singleton.add(key.toClass());
			bound = (List) singleton;
		}
		
		return bound; 
	}
	
	/**{@inheritDoc}*/
	@Override
	@SuppressWarnings("unchecked")
	public synchronized <T> T resolve(Class<T> clazz, List<? extends Object> args) {
		
		Constructor<T> constructor=null;
		
		//find constructor with bind annotation
		try {
			
			if (args.size()==0)
				constructor = clazz.getDeclaredConstructor();
			else
				loop: for (Constructor<?> c : clazz.getDeclaredConstructors()) {
					if (c.getAnnotation(Bind.class)!=null)
						constructor = (Constructor) c;
					else
						for(Annotation[] as :  c.getParameterAnnotations())
							for (Annotation a : as)
								if (a instanceof Bind) {
									constructor = (Constructor) c;
									break loop;
								}
				}
			
			if (constructor==null)
				throw new RuntimeException("no constructors are annotated with @Bind");
			
			constructor.setAccessible(true);
			return constructor.newInstance(args.toArray());
		}
		catch(Throwable t) {
			throw new RuntimeException(t);
		}
	}
	
	/**{@inheritDoc}*/
	public synchronized <T> void bind(Key<T> key, Class<? extends T> boundClass) {
		
		if (boundClass.isInterface() || Modifier.isAbstract(boundClass.getModifiers()))
			throw new RuntimeException(format("cannot bind to interfaces or abstract classes, such as %1s", boundClass));
		
			
		List<Class<?>> classes = bindings.get(key);
		if (classes==null) {
			classes = new LinkedList<Class<?>>();
			bindings.put(key,classes);
		}
		classes.add(boundClass);
	}

	/**{@inheritDoc}*/
	@Override
	public <T> void bind(Class<T> clazz, Class<? extends T> boundClass) {
		bind(clazz,null,boundClass);
		
	}

	/**{@inheritDoc}*/
	@Override
	public <T> void bind(Class<T> clazz, Class<? extends Annotation> qualifier, Class<? extends T> boundClass) {
		bind(newKey(clazz,qualifier),boundClass);
	}

	/**{@inheritDoc}*/
	@Override
	public <T> void bind(Literal<T> lit, Class<? extends T> boundClass) {
		bind(lit,null,boundClass);
		
	}

	/**{@inheritDoc}*/
	@Override
	public <T> void bind(Literal<T> lit, Class<? extends Annotation> qualifier, Class<? extends T> boundClass) {
		bind(newKey(lit,qualifier),boundClass);
	}
}
