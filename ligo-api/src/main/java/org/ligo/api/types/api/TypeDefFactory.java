/**
 * 
 */
package org.ligo.api.types.api;

/**
 * @author Fabio Simeoni
 *
 */
public interface TypeDefFactory {

	<TYPE> void register(Class<TYPE> type, final Class<? extends TYPE> impl);
	<TYPE> void register(TypeKey<TYPE> type, final Class<? extends TYPE> impl);
	<TYPE> void register(TypeKey<TYPE> type, TypeDefProvider<TYPE> g);
	
	<TYPE> TypeDef<TYPE> generate(Class<TYPE> key);
	<TYPE> TypeDef<TYPE> generate(TypeKey<TYPE> key);
	
}
