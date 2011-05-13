/**
 * 
 */
package org.ligo.api.types.impl;

import javax.inject.Inject;

import org.ligo.api.DefaultObjectFactory;
import org.ligo.api.ObjectFactory;
import org.ligo.api.types.api.TypeDef;
import org.ligo.api.types.api.TypeDefProvider;
import org.ligo.api.types.api.TypeKey;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultTypeDefFactory extends AbstractTypeDefFactory {
	
	ObjectFactory factory;
	
	public DefaultTypeDefFactory() {
		this(new DefaultObjectFactory());
	}
	
	@Inject
	public DefaultTypeDefFactory(ObjectFactory f) {
		
		factory=f;
		
		register(new TypeKey<String>(String.class), new TypeDefProvider<String>() {
			
			final PrimitiveDef<String> string = new PrimitiveDef<String>(new TypeKey<String>(String.class,null));
			
			public TypeDef<String> generate(TypeKey<String> key){
				return key.qualifier()==null?string:new PrimitiveDef<String>(key);
			}
		});
		
		register(new TypeKey<Integer>(Integer.class), new TypeDefProvider<Integer>() {
			
			final PrimitiveDef<Integer> integer = new PrimitiveDef<Integer>(new TypeKey<Integer>(Integer.class,null));
			
			public PrimitiveDef<Integer> generate(TypeKey<Integer> key){
				return key.qualifier()==null?integer:new PrimitiveDef<Integer>(key);
			}
		});
		
		register(new TypeKey<Object>(Object.class), new TypeDefProvider<Object>() {
			public TypeDef<Object> generate(TypeKey<Object> key) {
				return new DefaultObjectTypeDef<Object>(key,DefaultTypeDefFactory.this,factory);
			}
		});
	}

	
	/**{@inheritDoc}*/
	@Override
	public <TYPE> void register(TypeKey<TYPE> type, final Class<? extends TYPE> impl) {
		register(type,new TypeDefProvider<TYPE>() {
			@Override
			public TypeDef<TYPE> generate(TypeKey<TYPE> key) {
				return new DefaultObjectTypeDef<TYPE>(new TypeKey<TYPE>(impl,key.qualifier()), DefaultTypeDefFactory.this,factory);
			}
		});
		
	}

}
