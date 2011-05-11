/**
 * 
 */
package org.ligo.api;

import java.lang.annotation.Annotation;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultObjectFactory implements ObjectFactory {

	/**{@inheritDoc}*/
	@Override
	public <T> T create(Class<T> type, Annotation... annotations) {
		try {
			return type.newInstance();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

}
