/**
 * 
 */
package org.ligo.nodes.patterns;

import org.ligo.nodes.model.api.Leaf;
import org.ligo.nodes.model.api.Node;
import org.ligo.nodes.patterns.constraints.Constraint;

/**
 * @author Fabio Simeoni
 *
 */
public abstract class LeafPattern<T, C extends Constraint<? super T>> extends NodePattern {

	C constraint;
	
	public LeafPattern(C c) {
		constraint = c;
	}
	
	/**{@inheritDoc}*/
	@Override
	final public Node bind(Node n) {
		
		boolean mismatch = false;
		
		if (!Leaf.class.isAssignableFrom(n.getClass()))
			mismatch=true;
		
		Leaf leaf = Leaf.class.cast(n);
		
		try {
			T value = valueOf(leaf.value());
			if (!constraint.accepts(value))
				mismatch=true;			
		}
		catch(Exception e) {
			mismatch=true;
		}
		
		if (mismatch)
			throw new RuntimeException(this+" does not match node "+n);
	
		else
			return leaf;
	}
	
	
	/**
	 * Returns the typed equivalent of a leaf's value.
	 * @param s the value.
	 * @return the typed value.
	 */
	abstract protected T valueOf(String s);
	
	/**{@inheritDoc}*/
	@Override 
	public String toString() {
		return "%1s["+constraint+"]";
	}
}
