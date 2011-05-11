/**
 * 
 */
package org.ligo.api;

import javax.inject.Inject;


/**
 * @author Fabio Simeoni
 *
 */
public class DefaultTypedProjector<IN,OUT> implements TypedProjector<IN,OUT> {

	DefaultProjector<IN,OUT> inner;
	/**
	 * 
	 */
	@Inject
	public DefaultTypedProjector(ProjectionContext<IN,OUT> ctxt) {
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
}
