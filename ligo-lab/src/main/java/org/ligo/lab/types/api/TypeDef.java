/**
 * 
 */
package org.ligo.lab.types.api;

import org.ligo.lab.binders.TypeBinder;
import org.ligo.lab.data.DataProvider;


/**
 * @author Fabio Simeoni
 *
 */
public interface TypeDef<TYPE> extends TypeBinder<DataProvider[], TYPE> {

	TypeKey<TYPE> key();
	
	TYPE bind(DataProvider[] data);
}
