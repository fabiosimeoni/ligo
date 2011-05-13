/**
 * 
 */
package org.ligo.api.types.impl;

import java.util.HashMap;
import java.util.Map;

import org.ligo.api.types.api.TypeDef;
import org.ligo.api.types.api.TypeDefFactory;
import org.ligo.api.types.api.TypeDefProvider;
import org.ligo.api.types.api.TypeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractTypeDefFactory implements TypeDefFactory {

	static Logger logger = LoggerFactory.getLogger(AbstractTypeDefFactory.class);
	
	//provide type definitions for given keys
	static Map<TypeKey<?>,TypeDefProvider<?>> providers = new HashMap<TypeKey<?>,TypeDefProvider<?>>();
	
	//cached typed definitions for given keys
	static Map<TypeKey<?>,TypeDef<?>> cache = new HashMap<TypeKey<?>, TypeDef<?>>();
	
	
	/**{@inheritDoc}*/
	public <TYPE> TypeDef<TYPE> generate(Class<TYPE> type) {
		return generate(new TypeKey<TYPE>(type));
	}
	
	/**{@inheritDoc}*/
	@Override
	public <TYPE> TypeDef<TYPE> generate(TypeKey<TYPE> key) {
		
		//have we generated a definition for this key before?
		@SuppressWarnings("unchecked")  //typechecked at registration time
		TypeDef<TYPE> def = (TypeDef) cache.get(key);
		
		//delegate for the generation otherwise 
		if (def==null) {
		
			TypeDefProvider<TYPE> provider; 
			Class<?> type = key.type();
			do {
				@SuppressWarnings("unchecked")  //typechecked at registration time
				TypeDefProvider<TYPE > temp = (TypeDefProvider) providers.get(new TypeKey(type,key.qualifier()));
				provider = temp;
			}
			while (provider==null && ((type = type.getSuperclass())!=null));
				
			if (provider==null)
				throw new IllegalStateException("no type definition provider registered for "+key.type());
			
			else 
				def = provider.generate(key);
				logger.trace("cached type definition for {}",key);
				cache.put(key,def);
		}
		
		return def;
	}

	/**{@inheritDoc}*/
	public <TYPE> void register(Class<TYPE> type, final Class<? extends TYPE> impl) {
		
		register(new TypeKey<TYPE>(type),impl);
	}
	
	/**{@inheritDoc}*/
	@Override
	public <TYPE> void register(TypeKey<TYPE> type, TypeDefProvider<TYPE> provider) {
			
			if (providers.containsKey(type))
				throw new IllegalStateException(type+" already registered");
			else {
				logger.trace("registered {}",type);
				providers.put(type,provider);
			}
	}
		

}
