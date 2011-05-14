/**
 * 
 */
package org.ligo.api.types.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ligo.api.ObjectFactory;
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
public abstract class AbstractTypeDefFactory implements TypeDefFactory {

	static Logger logger = LoggerFactory.getLogger(AbstractTypeDefFactory.class);
	
	//cached typed definitions for given keys
	private static Map<TypeKey<?>,TypeDef<?>> cache = new HashMap<TypeKey<?>, TypeDef<?>>();

	private final ObjectFactory objectFactory;
	/**
	 * 
	 */
	protected AbstractTypeDefFactory(ObjectFactory f) {
		
		objectFactory = f;
		
		for (TypeDef<?> def : getPredefinedTypeDefs())
			cache(def);
	}
	
	/**
	 * @return the objectFactory
	 */
	@Override
	public <TYPE> TYPE getInstance(TypeKey<TYPE> key, List<Object> args) {
		return objectFactory.getInstance(key, args);
	}
	
	/**{@inheritDoc}*/
	public final <TYPE> TypeDef<TYPE> getTypeDef(Class<TYPE> type) {		
		return getTypeDef(new TypeKey<TYPE>(type));
	}
	
	
	
	/**{@inheritDoc}*/
	@Override
	public <TYPE> TypeDef<TYPE> getTypeDef(TypeKey<TYPE> key) {
		
		//have we generated a definition for this key before?
		@SuppressWarnings("unchecked")  //typechecked at registration time
		TypeDef<TYPE> def = (TypeDef) cache.get(key);
		
		//delegate for the generation otherwise 
		if (def==null) {
			
			//try factory;
			Class<? extends TYPE> type = objectFactory.getType(key);
			
			if (type==null)
				throw new IllegalStateException("no type definition registered for "+key.type());
			else {
				def= getObjectTypeDef(new TypeKey<TYPE>(type,key.qualifier()));
				cache(def);
			}	
		}
		else 
			logger.trace("reusing type definition for {}",key);
		
		return def;
	}
	
	void cache(TypeDef<?> def) {
		logger.trace("cached type definition for {}",def.key());
		cache.put(def.key(),def);
	}
	
	protected abstract List<TypeDef<?>> getPredefinedTypeDefs();
	
	protected abstract <TYPE> ObjectTypeDef<TYPE> getObjectTypeDef(TypeKey<TYPE> key);

}
