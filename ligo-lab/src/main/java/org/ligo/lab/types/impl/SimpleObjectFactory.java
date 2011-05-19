package org.ligo.lab.types.impl;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ligo.lab.types.api.ObjectFactory;
import org.ligo.lab.types.api.TypeKey;

public class SimpleObjectFactory implements ObjectFactory {

	private Map<TypeKey<?>,Class<?>> bindings = new HashMap<TypeKey<?>,Class<?>>();
	
	/**
	 * 
	 */
	public SimpleObjectFactory() {
		//predefined bindings
		addBinding(List.class,ArrayList.class);
		addBinding(Set.class,HashSet.class);
	}
	
	public <TYPE> void addBinding(Class<TYPE> type, Class<? extends TYPE> impl) {
		bindings.put(new TypeKey<TYPE>(type),impl);
	}
	
	public <TYPE> void addBinding(TypeKey<TYPE> key, Class<? extends TYPE> impl) {
		bindings.put(key,impl);
	}
	
	/**{@inheritDoc}*/
	@Override
	public <T> T getInstance(TypeKey<T> key, Object[] args, Constructor<? extends T> constructor) {

		try {
		
			if (constructor==null) {
			
				//lookup full key
				Class<? extends T> type = getType(key);
			
				constructor = findConstructor(type, args);
				
				
			}
	
			if (constructor != null)
				return constructor.newInstance(args);
			
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
			
		throw new RuntimeException("cannot instantiate "+key.type());

	}
	
	<T> Constructor<? extends T> findConstructor(Class<T> type, Object[] args) {
		
		List<Class<?>> types = new ArrayList<Class<?>>();
		for (Object arg : args)
			types.add(arg.getClass());
		
		Constructor<? extends T> constructor = null;
		
		outer:for (Constructor<?> c : type.getDeclaredConstructors()) {
			
			Class<?>[] params = c.getParameterTypes();
			
			if (params.length==types.size()) {
				for (int i=0; i<params.length;i++)
					if (!params[i].isAssignableFrom(types.get(i)))
						continue outer;
				
				if (constructor!=null) 
					throw new RuntimeException("cannot identify constructor:"+constructor+" and "+c+"  are both candidates");
				
				c.setAccessible(true);
					
				@SuppressWarnings("unchecked")
				Constructor<? extends T> cs = (Constructor) c;
				constructor = cs;
			}
		}
		
		return constructor;
		
	}
	@Override
	@SuppressWarnings("unchecked")
	public <T> Class<? extends T> getType(TypeKey<T> key) {
		
		//lookup full key
		Class<? extends T> type = (Class) bindings.get(key);
	
		if (type==null)
			//lookup raw binding
			type = (Class) bindings.get(new TypeKey<T>(key.type(),key.qualifier()));
		
		
		return type==null?key.type():type;
	
	}

}