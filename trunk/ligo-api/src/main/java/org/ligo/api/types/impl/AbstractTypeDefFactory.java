/**
 * 
 */
package org.ligo.api.types.impl;

import java.util.HashMap;
import java.util.Map;

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
			
			def = resolveTypeDef(key);
			
			if (def==null)
				throw new IllegalStateException("no type definition registered for "+key.type());
			else {
				logger.trace("cached type definition for {}",key);
				cache.put(key,def);
			}		
		}
		else 
			logger.trace("reusing type definition for {}",key);
		
		return def;
	}
	
	protected abstract <TYPE> TypeDef<TYPE> resolveTypeDef(TypeKey<TYPE> key);

		

}
