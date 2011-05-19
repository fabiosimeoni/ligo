/**
 * 
 */
package org.ligo.lab.types.impl;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ligo.lab.types.api.ObjectFactory;
import org.ligo.lab.types.api.TypeDef;
import org.ligo.lab.types.api.TypeDefConstructor;
import org.ligo.lab.types.api.TypeDefFactory;
import org.ligo.lab.types.api.TypeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni
 *
 */
public class BaseTypeDefFactory implements TypeDefFactory {

	static Logger logger = LoggerFactory.getLogger(BaseTypeDefFactory.class);
	
	//cached typed definitions for given keys
	private static Map<TypeKey<?>,TypeDef<?>> cache = new HashMap<TypeKey<?>, TypeDef<?>>();

	private final ObjectFactory objectFactory;
	
	private Map<Class<?>,TypeDefConstructor<?>> typedefConstructors = new HashMap<Class<?>, TypeDefConstructor<?>>();
	
	/**
	 * 
	 */
	public BaseTypeDefFactory(ObjectFactory f) {
		
		objectFactory = f;
	}
	
	public <TYPE> void addTypeDefConstructor(TypeDefConstructor<TYPE> constructor, Class<? extends TYPE> type) {
		
		typedefConstructors.put(type,constructor);

	}

	/**
	 * @return the objectFactory
	 */
	@Override
	public <TYPE> TYPE getInstance(TypeKey<TYPE> key, List<Object> args, Constructor<? extends TYPE> c) {
		return objectFactory.getInstance(key, args.toArray(new Object[0]),c);
	}
	
	/**{@inheritDoc}*/
	public final <TYPE> TypeDef<TYPE> getTypeDef(Class<TYPE> type) {		
		return getTypeDef(new TypeKey<TYPE>(type));
	}
	
	
	
	/**{@inheritDoc}*/
	@Override
	public <TYPE> TypeDef<TYPE> getTypeDef(TypeKey<TYPE> key) {
		
		//hit cache first
		@SuppressWarnings("unchecked")  //safe because insertion is type-checked
		TypeDef<TYPE> def = (TypeDef) cache.get(key);
		
		//if not a hit, delegate to type constructor 
		if (def==null) {
			
			//delegate
			@SuppressWarnings("unchecked") //safe because insertion is type-checked
			TypeDefConstructor<TYPE> constructor = (TypeDefConstructor) typedefConstructors.get(key.type());
				
			//if there is no specific constructor, delegate to generic object constructor
			if (constructor==null) {
	
				//use actual type from factory;
				Class<? extends TYPE> actualType = objectFactory.getType(key);
				
				//build key based on actual type
				TypeKey<TYPE> actualKey = new TypeKey<TYPE>(actualType,key.qualifier(),key.typeParameters());
				
				//delegate
				@SuppressWarnings("unchecked")
				TypeDefConstructor<TYPE> objectConstructor = (TypeDefConstructor) typedefConstructors.get(Object.class);
				
				def = objectConstructor.getTypeDef(actualKey, this);
			
			}
			
			else
			
				def = constructor.getTypeDef(key, this);
				
			
			cache(def);
			
		}
		else 
			logger.trace("reusing type definition for {}",key);
		
		return def;
	}
	
	void cache(TypeDef<?> def) {
		logger.trace("cached type definition for {}",def.key());
		cache.put(def.key(),def);
	}

}