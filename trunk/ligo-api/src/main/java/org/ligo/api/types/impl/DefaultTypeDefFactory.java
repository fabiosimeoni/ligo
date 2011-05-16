/**
 * 
 */
package org.ligo.api.types.impl;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.ligo.api.ObjectFactory;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultTypeDefFactory extends AbstractTypeDefFactory {
	
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
