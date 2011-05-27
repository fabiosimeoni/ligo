/**
 * 
 */
package org.ligo.lab.core.annotations;

import static org.ligo.lab.core.Key.*;
import static org.ligo.lab.core.kinds.Kind.*;

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
public class AdaptedBinder<INTYPE,OUTTYPE> extends AbstractBinder<OUTTYPE> {

	private static Logger logger = LoggerFactory.getLogger(AdaptedBinder.class);
	
	private final Environment env;
	private final BindingAdapter<INTYPE,OUTTYPE> adapter;
	private final Key<? extends INTYPE> inKey;
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected AdaptedBinder(BindingAdapter<INTYPE,OUTTYPE> a, Environment e) {
		super((Key)get(GENERIC(kindOf(a.getClass().getGenericSuperclass())).getActualTypeArguments()[1]));
		env=e;
		adapter=a;
		inKey = (Key) get(GENERIC(kindOf(a.getClass().getGenericSuperclass())).getActualTypeArguments()[0]);
		
	}

	/**{@inheritDoc}*/
	@Override
	public OUTTYPE bind(List<Provided> i) {
		System.out.println("binding intype "+inKey);
		INTYPE bound = env.binderFor(inKey).bind(i);
		OUTTYPE adapted = adapter.bindIn(bound);
		logger.trace("bound {} to {}",bound,adapted);
		return adapted;
	}
}