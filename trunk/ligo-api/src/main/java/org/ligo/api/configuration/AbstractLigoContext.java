/**
 * 
 */
package org.ligo.api.configuration;

import static java.lang.String.*;

import org.ligo.api.Binder;
import org.ligo.api.PatternFactory;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractLigoContext<IN,OUT> implements LigoContext<IN,OUT> {

	private final Binder<OUT> binder;
	private final PatternFactory<IN,OUT> factory;
	
	protected AbstractLigoContext(Binder<OUT> b,PatternFactory<IN,OUT> f) {
		binder=b;
		factory=f;
	}
	
	/**{@inheritDoc}*/
	@Override
	public Binder<OUT> binder() {
		// TODO Auto-generated method stub
		return binder;
	}

	/**{@inheritDoc}*/
	@Override
	public PatternFactory<IN, OUT> patternFactory() {
		return factory;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format("binder = %1s, pattern-factory = %2s)",binder,factory);
	}

}
