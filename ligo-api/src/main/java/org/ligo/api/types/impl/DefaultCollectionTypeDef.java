/**
 * 
 */
package org.ligo.api.types.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ligo.api.types.api.CollectionTypeDef;
import org.ligo.api.types.api.TypeDef;
import org.ligo.api.types.api.TypeDefFactory;
import org.ligo.api.types.api.TypeKey;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultCollectionTypeDef<C extends Collection<?>> extends AbstractTypeDef<C> implements CollectionTypeDef<C> {

	private TypeDef<?> member;
	private TypeDefFactory typeFactory;
	
	/**
	 * @param k
	 */
	public DefaultCollectionTypeDef(TypeKey<C> key,TypeDefFactory tf) {
		super(key);
		typeFactory=tf;
		
		build();
	}
	
	/**{@inheritDoc}*/
	@Override
	public TypeDef<?> memberType() {
		return member;
	}

	/**{@inheritDoc}*/
	@Override
	public C newInstance(Object data) {
		
		Object[] values = (Object[]) data;
		
		List<Object> temp = new ArrayList<Object>();
		
		if (values!=null)
			for (Object value : values)
				temp.add(member.newInstance(value));
		
		C list = typeFactory.getInstance(key(),temp);

		return list;
	}
	
	
	void build() {
		
		Type memberType = key().typeParameters()[0]; 
		
		if (!(memberType instanceof Class<?>))
			throw new RuntimeException("nested parametric types are not supported");
		
		Class<?> memberClass = (Class<?>) memberType;
		
		@SuppressWarnings("unchecked")
		TypeKey<?> memberkey = new TypeKey(memberClass,key().qualifier());
		
		member = typeFactory.getTypeDef(memberkey);
		
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return "coll["+member+"]";
	}
}
