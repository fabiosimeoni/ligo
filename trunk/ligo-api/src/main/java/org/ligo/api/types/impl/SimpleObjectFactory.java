package org.ligo.api.types.impl;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ligo.api.ObjectFactory;
import org.ligo.api.types.api.TypeKey;

public class SimpleObjectFactory implements ObjectFactory {

	private Map<TypeKey<?>,Class<?>> bindings = new HashMap<TypeKey<?>,Class<?>>();
	
	/**{@inheritDoc}*/
	@Override
	public <T> T getInstance(TypeKey<T> key, List<Object> args) {
		try {
			List<Class<?>> types = new ArrayList<Class<?>>();
			for (Object arg : args)
				types.add(arg.getClass());
			
			Constructor<? extends T> constructor = key.type().getDeclaredConstructor(types.toArray(new Class<?>[0]));
			constructor.setAccessible(true);
			return constructor.newInstance(args.toArray(new Object[0]));
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public <TYPE> void register(Class<TYPE> type, Class<? extends TYPE> impl) {
		bindings.put(new TypeKey<TYPE>(type),impl);
	}
	
	public <TYPE> void register(TypeKey<TYPE> key, Class<? extends TYPE> impl) {
		bindings.put(key,impl);
	}
	
	@Override
	public <T> Class<? extends T> getType(TypeKey<T> key) {
		
		if (key.type().isInterface()) {
			@SuppressWarnings("unchecked")
			Class<T> impl = (Class) bindings.get(key);
			return impl;
		}
		else
			return key.type();
	}

}