/**
 * 
 */
package org.ligo.api.types.impl;

import java.util.HashMap;
import java.util.Map;

import org.ligo.api.ObjectFactory;
import org.ligo.api.types.api.TypeDef;
import org.ligo.api.types.api.TypeKey;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultTypeDefFactory extends AbstractTypeDefFactory {
	
	private final ObjectFactory factory;
	
	private static Map<TypeKey<?>,TypeDef<?>> predefined = new HashMap<TypeKey<?>, TypeDef<?>>();
	
	public DefaultTypeDefFactory() {
		this(new SimpleObjectFactory());
	}
	
	public DefaultTypeDefFactory(ObjectFactory f) {
		
		factory=f;
		
		TypeKey<String> stringKey = new TypeKey<String>(String.class);
		predefined.put(stringKey, new PrimitiveDef<String>(stringKey));
		
		TypeKey<Integer> intKey = new TypeKey<Integer>(Integer.class);
		predefined.put(intKey, new PrimitiveDef<Integer>(intKey));
		
	}

	@Override
	public <TYPE> TypeDef<TYPE> resolveTypeDef(TypeKey<TYPE> key) {
		
		@SuppressWarnings("unchecked")
		TypeDef<TYPE> def = (TypeDef) predefined.get(key);
		
		if (def==null) {
			Class<? extends TYPE> type = factory.getType(key);
			def= type==null?
				null:
				new DefaultObjectTypeDef<TYPE>(new TypeKey<TYPE>(type,key.qualifier()), this,factory);
		}
		return def;
	}
	
}
