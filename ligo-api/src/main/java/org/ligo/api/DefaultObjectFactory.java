/**
 * 
 */
package org.ligo.api;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.ligo.api.types.api.TypeKey;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultObjectFactory implements ObjectFactory {

	/**{@inheritDoc}*/
	@Override
	public <T> T create(TypeKey<T> key, List<Object> args) {
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

}
