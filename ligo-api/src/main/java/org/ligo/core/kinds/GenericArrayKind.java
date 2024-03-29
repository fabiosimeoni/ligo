/**
 * 
 */
package org.ligo.core.kinds;

import static org.ligo.core.kinds.Kind.KindValue.*;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;

/**
 * @author Fabio Simeoni
 *
 */
public final class GenericArrayKind extends Kind<GenericArrayType> {
		
		public GenericArrayKind(GenericArrayType t) {
			super(t);
		}
		
		/**{@inheritDoc}*/
		@Override
		public KindValue value() {
			return GENERICARRAY;
		}

		/**{@inheritDoc}*/
		@Override
		public Class<?> toClass() {
			return Array.newInstance(kindOf(type().getGenericComponentType()).toClass(),0).getClass();
		}
		
		/**{@inheritDoc}*/
		@Override
		public String toString() {
			return kindOf(type().getGenericComponentType()).toString()+"[]";
		}
}
