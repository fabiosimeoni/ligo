/**
 * 
 */
package org.ligo.lab.typebinders;

import java.util.List;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.data.Provided;



/**
 * 
 * Ligo type-to-data {@link Binder}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface TypeBinder<TYPE> extends Binder<List<Provided>,TYPE> {

	Key<TYPE> key();

}
