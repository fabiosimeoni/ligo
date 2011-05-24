/**
 * 
 */
package org.ligo.lab.typebinders;

import java.util.Collection;


/**
 * @author Fabio Simeoni
 *
 */
public interface CollectionBinder<COLLTYPE extends Collection<TYPE>,TYPE> extends TypeBinder<COLLTYPE>{

	TypeBinder<?> part();
}
