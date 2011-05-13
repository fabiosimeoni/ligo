/**
 * 
 */
package org.ligo.api.types.impl;

import static java.lang.String.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Qualifier;

import org.ligo.api.ObjectFactory;
import org.ligo.api.Project;
import org.ligo.api.types.api.ObjectTypeDef;
import org.ligo.api.types.api.TypeDef;
import org.ligo.api.types.api.TypeDefFactory;
import org.ligo.api.types.api.TypeKey;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultObjectTypeDef<TYPE> extends AbstractTypeDef<TYPE> implements ObjectTypeDef<TYPE> {

	private Map<String,TypeDef<?>> parts = new HashMap<String, TypeDef<?>>();
	private ObjectFactory factory;
	private TypeDefFactory typeFactory;
	private ConstructorDef<TYPE> constructorDef;
	private List<MethodDef> methodDefs = new LinkedList<MethodDef>();
	
	public DefaultObjectTypeDef(TypeKey<TYPE> key, TypeDefFactory tf, ObjectFactory of) {
		
		super(key);
		typeFactory=tf;
		factory=of;
		
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
				
			TYPE object = factory.create(key(),vals);
		
			for (MethodDef def : methodDefs) {
				vals.clear();
				for (String name : def.names()) {
					Object part = parts.get(name).newInstance(values.get(name));
					vals.add(part);
				}
				def.method().setAccessible(true);
				def.method().invoke(object,vals.toArray(new Object[0]));				
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
	@SuppressWarnings("unchecked")
	void setConstructor() {
		
		//identify constructorDef
		for (Constructor<?> c : key().type().getDeclaredConstructors()) {
			List<String> boundNames = addAttributes(c.getParameterAnnotations(), c.getParameterTypes());
			if (boundNames.size()>0) {
				if (constructorDef==null)
					constructorDef = new ConstructorDef<TYPE>((Constructor)c,boundNames);
				else
					throw new RuntimeException(format("%1 has more than one constructorDef annotated for projection",key().type()));
			}	
		}
		
		//if no constructorDef is annotated, try to use nullary one
		if (constructorDef==null)
			try {
				constructorDef = new ConstructorDef<TYPE>(key().type().getDeclaredConstructor(),EMPTY_LIST);
			}
			catch(Throwable e) {
				throw new RuntimeException(format("%1 has no nullary or annotated constructors",key().type()));
			}
	}
	
	void setMethods() {
		
		Class<?> type = key().type();
		do 
		
			for (Method m : type.getDeclaredMethods()) {
				
				Class<?>[] params = m.getParameterTypes();
				List<String> boundNames = addAttributes(m.getParameterAnnotations(),params);
				if (boundNames.isEmpty()) {
					
					//try to find annotations in superclass
					Class<?> original = m.getDeclaringClass();
					if (original!=Object.class && !type.equals(original)) {
						System.out.println("loking for annotations of "+m);
						try {
							Method overridden = original.getMethod(m.getName(),params);	
							boundNames = addAttributes(overridden.getParameterAnnotations(),params);
						}
						catch(Exception e) {
							throw new RuntimeException(e);
						}
						
					}
					
					//try to find annotations in interface
					if (boundNames.isEmpty())
						for (Class<?> i : type.getInterfaces())
							try {
								Method overridden = i.getMethod(m.getName(),params);
								boundNames = addAttributes(overridden.getParameterAnnotations(),params);
							}
							catch(NoSuchMethodException e) {
								continue;
					}
				}
				
				if (!boundNames.isEmpty())
					methodDefs.add(new MethodDef(m,boundNames));
			}
		
		while //repeat for methodDefs in superclass
			((type=type.getSuperclass())!=null); 
		
		
	}
	
	List<String> addAttributes(Annotation[][] annotationLists, Class<?> parameters[]) {
		
		List<String> boundNames = new LinkedList<String>();
		for (int i =0; i<parameters.length;i++)
			for (Annotation annotation : annotationLists[i])
				if (annotation instanceof Project) {
					Project project = (Project) annotation;
					if (parts.containsKey(project.value()))
						throw new RuntimeException("projected name $1s is duplicated in "+key().type());
					else {	
						boundNames.add(project.value());
						@SuppressWarnings("unchecked") //must create a raw type for this key
						TypeDef<?> def = typeFactory.generate(new TypeKey(parameters[i],getQualifier(annotationLists[i])));
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
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return super.toString()+parts;
	}
}
