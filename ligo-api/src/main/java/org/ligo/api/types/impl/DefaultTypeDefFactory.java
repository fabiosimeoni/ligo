/**
 * 
 */
package org.ligo.api.types.impl;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.ligo.api.ObjectFactory;
import org.ligo.api.types.impl.DefaultTypeDefConstructors.CollectionDefConstructor;
import org.ligo.api.types.impl.DefaultTypeDefConstructors.ObjectDefConstructor;
import org.ligo.api.types.impl.DefaultTypeDefConstructors.PrimitiveDefConstructor;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultTypeDefFactory extends BaseTypeDefFactory {
	
	@Inject
	@SuppressWarnings("unchecked")
	public DefaultTypeDefFactory(ObjectFactory f) {	
		super(f);
		addTypeDefConstructor(new PrimitiveDefConstructor<Integer>(), Integer.class);
		addTypeDefConstructor(new PrimitiveDefConstructor<String>(), String.class);
		addTypeDefConstructor(new CollectionDefConstructor<List>(), List.class);
		addTypeDefConstructor(new CollectionDefConstructor<Set>(),Set.class);
		addTypeDefConstructor(new ObjectDefConstructor<Object>(), Object.class);
	}

}
