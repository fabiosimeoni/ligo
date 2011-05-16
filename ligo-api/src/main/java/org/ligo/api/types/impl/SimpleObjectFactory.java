package org.ligo.api.types.impl;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ligo.api.ObjectFactory;
import org.ligo.api.types.api.TypeKey;

public class SimpleObjectFactory implements ObjectFactory {

	private Map<TypeKey<?>,Class<?>> bindings = new HashMap<TypeKey<?>,Class<?>>();
	
	/**
	 * 
	 */
	public SimpleObjectFactory() {
		//predefined bindings
		register(List.class,ArrayList.class);
		register(Set.class,HashSet.class);
	}
	
	/**{@inheritDoc}*/
	@Override
	public <T> T getInstance(TypeKey<T> key, Object ... args) {

		
			//lookup full key
			Class<? extends T> type = getType(key);
			
			List<Class<?>> types = new ArrayList<Class<?>>();
			for (Object arg : args)
				types.add(arg.getClass());
		try {
		
			//find constructor that matches argument, if any (cannot use getConstructor as it does not respect subtyping)
			outer:for (Constructor<?> c : type.getDeclaredConstructors()) {
				Class<?>[] params = c.getParameterTypes();
				if (params.length==types.size()) {
					for (int i=0; i<params.length;i++)
						if (!params[i].isAssignableFrom(types.get(i)))
							continue outer;
					@SuppressWarnings("unchecked")
					Constructor<? extends T> constructor = (Constructor) c;
					constructor.setAccessible(true);
					return constructor.newInstance(args);
				}
			}
		
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
			
		throw new RuntimeException("cannot instantiate "+key.type());

	}
	
	public <TYPE> void register(Class<TYPE> type, Class<? extends TYPE> impl) {
		bindings.put(new TypeKey<TYPE>(type),impl);
	}
	
	public <TYPE> void register(TypeKey<TYPE> key, Class<? extends TYPE> impl) {
		bindings.put(key,impl);
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