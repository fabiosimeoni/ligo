/**
 * 
 */
package org.ligo.lab.types;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.binders.TypeBinderFactory;
import org.ligo.lab.data.DataProvider;
import org.ligo.lab.types.api.TypeDefFactory;

/**
 * @author Fabio Simeoni
 *
 */
public class TypeDefBinderFactory<TYPE> implements TypeBinderFactory<DataProvider[],TYPE> {

	private TypeDefFactory factory;
	
	public TypeDefBinderFactory(TypeDefFactory f) {
		factory=f;
	}
	
	/**{@inheritDoc}*/
	@Override
	public Binder<DataProvider[], TYPE> bind(Class<TYPE> type) {
		return factory.getTypeDef(type);
	}
}
