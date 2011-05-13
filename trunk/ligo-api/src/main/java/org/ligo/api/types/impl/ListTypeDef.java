/**
 * 
 */
package org.ligo.api.types.impl;

import java.lang.reflect.TypeVariable;

import org.ligo.api.ObjectFactory;
import org.ligo.api.types.api.CollectionTypeDef;
import org.ligo.api.types.api.TypeDef;
import org.ligo.api.types.api.TypeDefFactory;
import org.ligo.api.types.api.TypeKey;

/**
 * @author Fabio Simeoni
 *
 */
public class ListTypeDef<TYPE> extends AbstractTypeDef<TYPE> implements CollectionTypeDef<TYPE> {

	private TypeDef<?> member;
	private ObjectFactory factory;
	private TypeDefFactory typeFactory;
	
	/**
	 * @param k
	 */
	protected ListTypeDef(TypeKey<TYPE> k,TypeDefFactory tf, ObjectFactory of) {
		super(k);
		build();
		typeFactory=tf;
		factory=of;
	}
	
	/**{@inheritDoc}*/
	@Override
	public TypeDef<?> member() {
		// TODO Auto-generated method stub
		return null;
	}

	/**{@inheritDoc}*/
	@Override
	public TYPE newInstance(Object data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	void build() {
		
		//TypeVariable<Class<? extends TYPE>>[] generics = key().type().getTypeParameters();
		
		//@SuppressWarnings("unchecked")
		//TypeDef<?> member = typeFactory.generate(new Typekey());
	}
}
