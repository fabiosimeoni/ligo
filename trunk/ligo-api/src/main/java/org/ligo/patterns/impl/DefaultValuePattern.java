/**
 * 
 */
package org.ligo.patterns.impl;

import org.ligo.data.LigoData;
import org.ligo.data.LigoValue;
import org.ligo.patterns.api.Constraint;
import org.ligo.patterns.api.ValuePattern;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class DefaultValuePattern<T, C extends Constraint<? super T>> extends AbstractLigoPattern implements ValuePattern<T, C> {

	C constraint;
	
	public DefaultValuePattern(C c) {
		constraint = c;
	}
	
	/**{@inheritDoc}*/
	@Override
	final public LigoData bind(LigoData data) {
		
		boolean mismatch = false;
		
		if (! (data instanceof LigoValue))
			mismatch=true;
		
		LigoValue value = (LigoValue) data;
		
		try {
			@SuppressWarnings("unchecked")
			T typedValue = (T) value.get();
			
			if (!constraint.accepts(typedValue))
				mismatch=true;			
		}
		catch(Exception e) {
			mismatch=true;
		}
		
		if (mismatch)
			throw new RuntimeException(this+" does not match "+data);
	
		else
			return value;
	}
	
	/**{@inheritDoc}*/
	@Override 
	public String toString() {
		return "%1s["+constraint+"]";
	}
}
