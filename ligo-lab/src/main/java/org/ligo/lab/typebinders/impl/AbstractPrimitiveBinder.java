/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static java.lang.String.*;

import java.util.List;

import org.ligo.lab.data.DataProvider;
import org.ligo.lab.data.Provided;
import org.ligo.lab.data.ValueProvider;
import org.ligo.lab.typebinders.Key;


/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractPrimitiveBinder<TYPE> extends AbstractTypeBinder<TYPE> {
	
	public AbstractPrimitiveBinder(Key<TYPE> c) {
		super(c);
	}
	
	public TYPE bind(List<Provided> provided) {
		
		try {
			

			if (provided.size()!=1)
				throw new RuntimeException("expected one value but found "+provided);
			
			DataProvider provider = provided.get(0).provided();
			
			if (!(provider instanceof ValueProvider))
				throw new RuntimeException("expected a value provider but found "+provided);
		
			
			ValueProvider vp = (ValueProvider) provider;
			
			return accept(vp.get());
		}
		catch(ClassCastException e) {
			throw new RuntimeException(format("cannot bind %1s to %2s",key(),provided),e);
		}
	};

	protected abstract TYPE accept(Object o);

}
