/**
 * 
 */
package org.ligo.lab.typebinders.kinds;

import static org.ligo.lab.typebinders.kinds.Kind.KindValue.*;

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
			return kindOf(type().getGenericComponentType()).toClass();
		}
		
		/**{@inheritDoc}*/
		@Override
		public String toString() {
			return kindOf(type().getGenericComponentType()).toString();
		}
}
