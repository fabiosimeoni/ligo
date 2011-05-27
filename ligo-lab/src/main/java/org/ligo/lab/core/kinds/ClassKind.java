/**
 * 
 */
package org.ligo.lab.core.kinds;

import static org.ligo.lab.core.kinds.Kind.KindValue.*;

/**
 * @author Fabio Simeoni
 *
 */
public final class ClassKind extends Kind<Class<?>> {
		
		/**
		 * 
		 */
		public ClassKind(Class<?> t) {
			super(t);
		}
		
		/**{@inheritDoc}*/
		@Override
		public KindValue value() {
			return CLASS;
		}
		
		/**{@inheritDoc}*/
		@Override
		public Class<?> toClass() {
			return type();
		}
		
		/**{@inheritDoc}*/
		@Override
		public String toString() {
			return type().getName();
		}
}
