/**
 * 
 */
package org.ligo.lab.typebinders.kinds;

import static org.ligo.lab.typebinders.kinds.Kind.KindValue.*;

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
		public String toString() {
			return type().getSimpleName();
		}
}
