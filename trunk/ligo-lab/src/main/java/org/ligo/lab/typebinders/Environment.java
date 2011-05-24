/**
 * 
 */
package org.ligo.lab.typebinders;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;



/**
 * @author Fabio Simeoni
 *
 */
public interface Environment {

	<T> TypeBinder<T> binder(Key<T> key);

	void addVariable(TypeVariable<?> var,Type t);

	TypeResolver resolver();
	
	
}
