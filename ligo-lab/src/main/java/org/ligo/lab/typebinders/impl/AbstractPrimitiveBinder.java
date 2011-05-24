/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static java.lang.String.*;

import java.util.List;

import org.ligo.lab.data.DataProvider;
import org.ligo.lab.data.Provided;
import org.ligo.lab.data.ValueProvider;
import org.ligo.lab.typebinders.Environment;
import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.TypeBinder;


/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractPrimitiveBinder<TYPE> extends AbstractBinder<TYPE> {
	
	public AbstractPrimitiveBinder(Key<TYPE> c) {
		super(c);
	}
	
	public TYPE bind(List<Provided> provided) {
		
		try {
			
			if (provided.size()!=1)
				switch(mode()) {
					
					case STRICT:
						throw new RuntimeException("expected one value but found: "+provided);
					
					case LAX:
						return null;
				}
			
			DataProvider dp = provided.get(0).provider();
		
			if (!(dp instanceof ValueProvider))
				switch(mode()) {
						
					case STRICT:
						throw new RuntimeException("expected a scalar but found: "+provided);
						
					case LAX:
						return null;
				}
		
			ValueProvider vp = (ValueProvider) dp;
			
			return accept(vp.get());
			
		}
		catch(ClassCastException e) {
			throw new RuntimeException(format("cannot bind %1s to %2s",key(),provided),e);
		}
	};
	

	protected abstract TYPE accept(Object o);
	
	BinderProvider<TYPE> provider() {
		return new BinderProvider<TYPE>() {
			@Override public Key<TYPE> matchingKey() {
				return key();
			}
			/**{@inheritDoc}*/
			@Override
			public TypeBinder<TYPE> binder(Key<TYPE> key, Environment factory) {
				return AbstractPrimitiveBinder.this;
			}
		};
	}

}
