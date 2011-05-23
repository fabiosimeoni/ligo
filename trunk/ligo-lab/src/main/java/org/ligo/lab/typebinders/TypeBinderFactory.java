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
public interface TypeBinderFactory {

	<T> TypeBinder<T> binder(Key<T> key);
	
	TypeResolver resolver();
	
	void addVariable(TypeVariable<?> var,Type t);
	//Type get(TypeVariable<?> var);
	
	//<TYPE> TYPE getInstance(Key<TYPE> key, List<Object> args, Constructor<? extends TYPE> c);
	
}
