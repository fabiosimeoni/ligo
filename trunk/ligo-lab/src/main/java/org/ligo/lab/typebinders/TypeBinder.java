/**
 * 
 */
package org.ligo.lab.typebinders;

import java.util.List;

import org.ligo.lab.binders.Binder;
import org.ligo.lab.data.Provided;
import org.ligo.lab.typebinders.Bind.Mode;



/**
 * 
 * Ligo type-to-data {@link Binder}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface TypeBinder<TYPE> extends Binder<List<Provided>,TYPE> {

	Key<TYPE> key();
	
	void setMode(Mode m);

}
