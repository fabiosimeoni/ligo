/**
 * 
 */
package org.ligo.lab.types.api;

import java.util.Collection;




/**
 * @author Fabio Simeoni
 *
 */
public interface CollectionTypeDef<C extends Collection<?>> extends TypeDef<C> {
	
	TypeDef<?> memberType();

}
