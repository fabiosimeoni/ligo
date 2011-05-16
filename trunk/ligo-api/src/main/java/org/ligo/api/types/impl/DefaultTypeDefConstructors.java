/**
 * 
 */
package org.ligo.api.types.impl;

import java.util.Collection;

import org.ligo.api.types.api.TypeDef;
import org.ligo.api.types.api.TypeDefConstructor;
import org.ligo.api.types.api.TypeDefFactory;
import org.ligo.api.types.api.TypeKey;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultTypeDefConstructors {

	public static class CollectionDefConstructor<C extends Collection<?>> implements TypeDefConstructor<C> {
		/**{@inheritDoc}*/
		@Override
		public TypeDef<C> getTypeDef(TypeKey<C> key, TypeDefFactory factory) {
			return new DefaultCollectionTypeDef<C>(key,factory);
		}
	}
	
	public static class PrimitiveDefConstructor<T> implements TypeDefConstructor<T> {
		/**{@inheritDoc}*/
		@Override
		public TypeDef<T> getTypeDef(TypeKey<T> key, TypeDefFactory factory) {
			return new PrimitiveTypeDef<T>(key);
		}
	}
	
	public static class ObjectDefConstructor<T> implements TypeDefConstructor<T> {
		/**{@inheritDoc}*/
		@Override
		public TypeDef<T> getTypeDef(TypeKey<T> key, TypeDefFactory factory) {
			return new DefaultObjectTypeDef<T>(key,factory);
		}
	}
}
