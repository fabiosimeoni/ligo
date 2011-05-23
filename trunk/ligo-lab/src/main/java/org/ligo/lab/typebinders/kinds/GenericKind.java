/**
 * 
 */
package org.ligo.lab.typebinders.kinds;


import static org.ligo.lab.typebinders.kinds.Kind.KindValue.*;

import java.lang.reflect.ParameterizedType;

/**
 * @author Fabio Simeoni
 *
 */
public final class GenericKind extends Kind<ParameterizedType> {
		
		public GenericKind(ParameterizedType t) {
			super(t);
		}
		
		/**{@inheritDoc}*/
		@Override
		public KindValue value() {
			return GENERIC;
		}
	
		/**{@inheritDoc}*/
		@Override
		public String toString() {
			return kindOf(type().getRawType()).toString();
		}
}
