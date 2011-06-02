/**
 * 
 */
package org.ligo.core.binders.impl;

import static java.lang.String.*;

import java.util.LinkedList;
import java.util.List;

import org.ligo.core.binders.Environment;
import org.ligo.core.binders.TypeBinder;
import org.ligo.core.binders.UnionBinder;
import org.ligo.core.keys.Key;
import org.ligo.data.LigoData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Fabio Simeoni
 *
 */
public class DefaultUnionBinder<T> extends AbstractBinder<T> implements UnionBinder<T> {

	private static Logger logger = LoggerFactory.getLogger(DefaultUnionBinder.class);
	
	static final String TO_STRING= "%1s-union(%2s)";
	static final String ALL_BINDINGS_ERROR = "[%1s] could not bind any of %2s to %3s";
	
	private final List<TypeBinder<T>> branches;
	/**
	 * 
	 */
	public DefaultUnionBinder(Key<? extends T> key,Environment env, List<TypeBinder<T>> binders) {
		super(key);
		branches = binders;
	}
	
	/**{@inheritDoc}*/
	@Override
	public List<TypeBinder<T>> binders() {
		return new LinkedList<TypeBinder<T>>(branches);
	}
	
	/**{@inheritDoc}*/
	@Override
	public T bind(List<LigoData> i) {
		
		for (TypeBinder<T> branch : branches)
			try {
				branch.setMode(mode());		
				return branch.bind(i);
			}
			catch(RuntimeException e) {
				if (branches.size()>1)
					logger.warn(format(BINDING_ERROR,branch,i));
				else throw e;
			}
		
			throw new RuntimeException(branches.size()>1?
										format(ALL_BINDINGS_ERROR,branches,i):
										format(BINDING_ERROR,branches.get(0),i));
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return branches.size()>1?format(TO_STRING,super.toString(),branches):branches.get(0).toString();
	}
}
