/**
 * 
 */
package org.ligo.api.types.api;

import java.lang.reflect.Constructor;
import java.util.List;


/**
 * @author Fabio Simeoni
 *
 */
public interface TypeDefFactory {

	<TYPE> TypeDef<TYPE> getTypeDef(Class<TYPE> key);
	<TYPE> TypeDef<TYPE> getTypeDef(TypeKey<TYPE> key);
	
	<TYPE> TYPE getInstance(TypeKey<TYPE> key, List<Object> args, Constructor<? extends TYPE> c);
	
}
