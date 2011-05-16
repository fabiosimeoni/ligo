/**
 * 
 */
package org.ligo.api.types.api;

import org.ligo.api.data.DataProvider;


/**
 * @author Fabio Simeoni
 *
 */
public interface TypeDef<TYPE> {

	TypeKey<TYPE> key();
	
	TYPE newInstance(DataProvider ... data);
}
