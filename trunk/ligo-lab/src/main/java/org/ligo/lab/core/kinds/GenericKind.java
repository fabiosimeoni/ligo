/**
 * 
 */
package org.ligo.lab.core.kinds;


import static org.ligo.lab.core.kinds.Kind.KindValue.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
		public Class<?> toClass() {
			return (Class<?>) type().getRawType();
		}
		/**{@inheritDoc}*/
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(kindOf(type().getRawType())).append("<");
			Type[] types = type().getActualTypeArguments();
			for (int i=0;i<types.length;i++) {
				if (i>0)
					builder.append(",");
				builder.append(kindOf(types[i]));
			}
			builder.append(">");
			return builder.toString();	
		}
}
