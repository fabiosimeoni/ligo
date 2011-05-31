/**
 * 
 */
package org.ligo.core.annotations;

import static org.ligo.core.keys.Keys.*;

import java.util.List;

import org.ligo.core.Environment;
import org.ligo.core.TypeBinder;
import org.ligo.core.data.Provided;
import org.ligo.core.impl.AbstractBinder;
import org.ligo.core.impl.BindingAdapter;
import org.ligo.core.keys.Key;
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
	private final TypeBinder<INTYPE> inBinder;
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected AdaptedBinder(BindingAdapter<INTYPE,OUTTYPE> a, Environment e) {
		super((Class) a.outKind().toClass()); //raw bound type
		env=e;
		adapter=a;
		inBinder = env.binderFor((Key)newKey(a.inKind().type()));
		
	}

	/**{@inheritDoc}*/
	@Override
	public OUTTYPE bind(List<Provided> i) {
		INTYPE bound = inBinder.bind(i);
		OUTTYPE adapted = adapter.bind(bound);
		logger.trace("bound {} to {}",bound,adapted);
		return adapted;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return inBinder.toString();
	}
}