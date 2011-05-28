/**
 * 
 */
package org.ligo.lab.core.annotations;

import java.util.List;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.Key;
import org.ligo.lab.core.data.Provided;
import org.ligo.lab.core.impl.AbstractBinder;
import org.ligo.lab.core.impl.BindingAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni
 *
 */
class AdaptedBinder<INTYPE,OUTTYPE> extends AbstractBinder<OUTTYPE> {

	private static Logger logger = LoggerFactory.getLogger(AdaptedBinder.class);
	
	private final Environment env;
	private final BindingAdapter<INTYPE,OUTTYPE> adapter;
	private final Key<? extends INTYPE> inKey;
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected AdaptedBinder(BindingAdapter<INTYPE,OUTTYPE> a, Environment e) {
		super((Class) a.outKind().toClass()); //raw bound type
		env=e;
		adapter=a;
		inKey = new Key<INTYPE>(a.inKind());
		
	}

	/**{@inheritDoc}*/
	@Override
	public OUTTYPE bind(List<Provided> i) {
		INTYPE bound = env.binderFor(inKey).bind(i);
		OUTTYPE adapted = adapter.bind(bound);
		logger.trace("bound {} to {}",bound,adapted);
		return adapted;
	}
}