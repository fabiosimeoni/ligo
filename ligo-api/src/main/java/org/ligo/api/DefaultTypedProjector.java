/**
 * 
 */
package org.ligo.api;

import javax.inject.Inject;

import org.ligo.api.configuration.LigoContext;


/**
 * @author Fabio Simeoni
 *
 */
public class DefaultTypedProjector<IN,OUT> implements TypedProjector<IN,OUT> {

	Projector inner;
	
	@Inject
	public DefaultTypedProjector(Projector p) {
		inner = p;
	}
	
	/**
	 * 
	 */
	@Inject
	public DefaultTypedProjector(LigoContext<IN,OUT> ctxt) {
		inner = new DefaultProjector<IN,OUT>(ctxt);
	}
	
	public <T, M extends IN> T project(Class<T> c, M m, Pattern<M,? extends OUT> p) {
		return inner.project(c, m,p);
	};

	/**{@inheritDoc}*/
	@Override
	public <T> T project(Class<T> c, IN m) {
		return inner.project(c, m);
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return inner.toString();
	}
}
