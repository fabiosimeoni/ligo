/**
 * 
 */
package org.ligo.api.types.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.ligo.api.ObjectFactory;
import org.ligo.api.types.api.ObjectTypeDef;
import org.ligo.api.types.api.TypeDef;
import org.ligo.api.types.api.TypeKey;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultTypeDefFactory extends AbstractTypeDefFactory {
	
	@Inject
	public DefaultTypeDefFactory(ObjectFactory f) {	
		super(f);
	}

	/**{@inheritDoc}*/
	@Override
	protected List<TypeDef<?>> getPredefinedTypeDefs() {
		List<TypeDef<?>> defs = new ArrayList<TypeDef<?>>();
		defs.add(new PrimitiveTypeDef<String>(new TypeKey<String>(String.class)));
		defs.add(new PrimitiveTypeDef<Integer>(new TypeKey<Integer>(Integer.class)));
		return defs;
	}
	
	/**{@inheritDoc}*/
	@Override
	protected <TYPE> ObjectTypeDef<TYPE> getObjectTypeDef(TypeKey<TYPE> key) {
		return new DefaultObjectTypeDef<TYPE>(key,this);
	}
}
