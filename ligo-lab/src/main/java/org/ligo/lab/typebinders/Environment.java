/**
 * 
 */
package org.ligo.lab.typebinders;

import java.lang.reflect.TypeVariable;



/**
 * @author Fabio Simeoni
 *
 */
public interface Environment {

	<T> TypeBinder<T> bind(Key<T> key);

	void bindVariable(TypeVariable<?> var,TypeBinder<?> binder);

	Resolver resolver();
	
	
}
