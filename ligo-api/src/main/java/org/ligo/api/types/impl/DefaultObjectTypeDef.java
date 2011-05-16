/**
 * 
 */
package org.ligo.api.types.impl;

import static java.lang.String.*;
import static java.util.Arrays.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Qualifier;

import org.ligo.api.Project;
import org.ligo.api.types.api.ObjectTypeDef;
import org.ligo.api.types.api.TypeDef;
import org.ligo.api.types.api.TypeDefFactory;
import org.ligo.api.types.api.TypeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultObjectTypeDef<TYPE> extends AbstractTypeDef<TYPE> implements ObjectTypeDef<TYPE> {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultObjectTypeDef.class);
	
	private Map<String,TypeDef<?>> parts = new HashMap<String, TypeDef<?>>();
	private TypeDefFactory typeFactory;
	private ConstructorDef<TYPE> constructorDef;
	private List<MethodDef> methodDefs = new LinkedList<MethodDef>();
	
	public DefaultObjectTypeDef(TypeKey<TYPE> key, TypeDefFactory tf) {
		
		super(key);
		typeFactory=tf;
		
		build();
	}

	/**{@inheritDoc}*/
	@Override
	public TYPE newInstance(Object value) {
		
		try {
		
			@SuppressWarnings("unchecked")
			Map<String,Object> values = (Map) value;
			
			List<Object> vals = new LinkedList<Object>();
			
			//extract constructor parameters and off-load creation to factory
			for (String name : constructorDef.names())
				vals.add(parts.get(name).newInstance(values.get(name)));
				
			TYPE object = typeFactory.getInstance(key(),vals,constructorDef.constructor());
		
			for (MethodDef m : methodDefs) {
				vals.clear();
				for (String name : m.names()) {
					Object part = parts.get(name).newInstance(values.get(name));
					vals.add(part);
				}
				
				m.method().invoke(object,vals.toArray(new Object[0]));				
			}
			
			return object;
		}
		catch(Throwable e) {
			throw new RuntimeException(format("cannot bind %1s to %2s",key().type(),value),e);
		}
	}

	/**{@inheritDoc}*/
	@Override
	public Map<String,TypeDef<?>> attributes() {
		return parts;
	}
	
	void build() {
		
		logger.trace("building type definition from "+key().type());
		
		try {
			setConstructor();
			setMethods();
		}
		catch(Exception e) {
			throw new RuntimeException("could not generate type definition from "+key().type(),e);
		}
	}
	
	
	/**
	 * @param constructorDef the constructorDef to set
	 */
	void setConstructor() {
		
		List<String> boundNames = new ArrayList<String>();
		Constructor<?> constructor=null;
		//identify constructorDef
		for (Constructor<?> c : key().type().getDeclaredConstructors()) {
			boundNames = addAttributes(c.getParameterAnnotations(), c.getGenericParameterTypes());
			if (boundNames.size()>0) {
				if (constructor==null)
					constructor=c;
				else
					throw new RuntimeException(format("%1 has more than one constructorDef annotated for projection",key().type()));
			}	
		}
		
		//if no constructorDef is annotated, try to use nullary one
		if (constructor==null)
			try {
				constructor = key().type().getDeclaredConstructor();
			}
			catch(Throwable e) {
				throw new RuntimeException(format("%1s has no nullary or annotated constructors",key().type()));
			
			}
		
		constructor.setAccessible(true);
		
		@SuppressWarnings("unchecked")
		ConstructorDef<TYPE> def = new ConstructorDef<TYPE>((Constructor) constructor,boundNames);
		
		constructorDef=def;
		
	}
	
	void setMethods() {
		
		Class<?> type = key().type();
		
		do 
		
			for (Method m : type.getDeclaredMethods()) {
				
				Type[] params = m.getGenericParameterTypes();
				List<String> boundNames = addAttributes(m.getParameterAnnotations(),params);
				if (boundNames.isEmpty())
					//look in interfaces
					for (Class<?> i : type.getInterfaces())
						try {
							Method overridden = i.getMethod(m.getName(),toRawType(params));
							boundNames = addAttributes(overridden.getParameterAnnotations(),params);
						}
						catch(NoSuchMethodException e) {
							continue;
						}
				if (!boundNames.isEmpty()) {
			
					if (Modifier.isPrivate(m.getModifiers()))
						throw new RuntimeException("cannot project over private method "+m);
					
					m.setAccessible(true);
					methodDefs.add(new MethodDef(m,boundNames));
				}
			}
		
		while //repeat for methodDefs in superclass
			((type=type.getSuperclass())!=null);
		
		
	}
	
	Class<?>[] toRawType(Type[] ts) {
		
		List<Class<?>> raws = new ArrayList<Class<?>>();
		for (Type t : ts)
			raws.add(toRawType(t));
		return raws.toArray(new Class<?>[0]);
	}
	
	Class<?> toRawType(Type t) {
		
		if (t instanceof Class<?>)
			return (Class<?>) t;
		else if (t instanceof ParameterizedType)
			return ((ParameterizedType) t).getRawType().getClass();
		else
			throw new RuntimeException("unsupported type "+t);
	}
	
	List<String> addAttributes(Annotation[][] annotationLists, Type parameters[]) {
		
		List<String> boundNames = new LinkedList<String>();
		for (int i =0; i<parameters.length;i++)
			for (Annotation annotation : annotationLists[i])
				if (annotation instanceof Project) {
					
					Project project = (Project) annotation;
					
					if (parts.containsKey(project.value()))
						throw new RuntimeException("projected name $1s is duplicated in "+key().type());
					else {	
						boundNames.add(project.value());
						TypeDef<?> def = 
							typeFactory.getTypeDef(buildParamKey(parameters[i],getQualifier(annotationLists[i])));
						
						parts.put(project.value(),def);
					}
					break;
				}
		
		return boundNames;
	}
	
	Annotation getQualifier(Annotation[] annotations) {
		for (Annotation a : annotations)
			if (a.annotationType().isAnnotationPresent(Qualifier.class))
				return a;
		return null;
	}
	
	private TypeKey<?> buildParamKey(Type param, Annotation qualifier) {
		
		Class<?> rawType = null;
		Map<TypeVariable<?>,Type> typeParams = null;
		if (param instanceof Class<?>)
			rawType = (Class<?>) param;
		
		else if (param instanceof ParameterizedType) {
		
			ParameterizedType generic = (ParameterizedType) param;
			rawType = (Class<?>) generic.getRawType();
			typeParams = new HashMap<TypeVariable<?>, Type>();
			Iterator<? extends TypeVariable<?>> vars =asList(rawType.getTypeParameters()).iterator();
			Iterator<Type> types = asList(generic.getActualTypeArguments()).iterator();
			while (vars.hasNext()) 
				typeParams.put(vars.next(), types.next());
		}
		else if (param instanceof TypeVariable<?>) {
		
			TypeVariable<?> var = (TypeVariable<?>) param;
			Type value = key().typeParameters().get(var);
			if (value instanceof Class<?>) {
				rawType = (Class<?>) key().typeParameters().get(var);
				if (rawType==null)
					throw new RuntimeException(var.getName()+" is unbound in "+key().typeParameters());	
			}
			else
				rawType = buildParamKey(value, qualifier).type(); //TODO: we lose type parameter of variable value here!
		}
		
		@SuppressWarnings("unchecked") //must create a raw type for this key
		TypeKey<?> key = new TypeKey(rawType,qualifier,typeParams);
		
		return key;
	}
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return "obj"+parts;
	}
}
