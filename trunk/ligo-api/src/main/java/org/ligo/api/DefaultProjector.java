/**
 * 
 */
package org.ligo.api;

import static java.lang.String.*;

import javax.inject.Inject;

import org.ligo.api.configuration.LigoContext;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultProjector<MODEL,MATCH> implements Projector {

	LigoContext<MODEL, MATCH> context;
	
	/**
	 * 
	 */
	@Inject
	public DefaultProjector(LigoContext<MODEL, MATCH> c) {
		context=c;
	}
	
	/**{@inheritDoc}*/
	public <T> T project(Class<T> c, Object m) {
		
		//runtime check
		if (!context.modelType().isAssignableFrom(m.getClass()))
			throw new RuntimeException("expected "+context.modelType()+" found "+m.getClass().toString());
		
		MODEL model = context.modelType().cast(m);
		
		return project(c,model,context.patternFactory().generate(c));
		
	}
	
	/**{@inheritDoc}*/
	public <T,M> T project(Class<T> c, M m, Pattern<M,? extends Object> p) {
		
		Object untypedMatch = p.extract(m);
		
		//runtime check
		if (!context.matchType().isAssignableFrom(untypedMatch.getClass()))
			throw new RuntimeException("expected "+context.matchType()+" found "+untypedMatch.getClass().toString());
		
		MATCH match = context.matchType().cast(untypedMatch);
		
		return context.binder().bind(c,match);
		
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format("(%1s)",context);
	}
}
