/**
 * 
 */
package org.ligo.core.kinds;

import static org.ligo.core.kinds.Kind.KindValue.*;

import java.lang.reflect.TypeVariable;

/**
 * @author Fabio Simeoni
 *
 */
public final class VarKind extends Kind<TypeVariable<?>> {
		
		public VarKind(TypeVariable<?> t) {
			super(t);
		}
		
		/**{@inheritDoc}*/
		@Override
		public KindValue value() {
			return TYPEVAR;
		}

		/**{@inheritDoc}*/
		@Override
		public Class<?> toClass() {
			return null;
		}
		
		/**{@inheritDoc}*/
		@Override
		public String toString() {
			return type().getName()+"-of-"+((Class<?>)type().getGenericDeclaration()).getSimpleName();
		}
}
