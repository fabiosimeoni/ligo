/**
 * 
 */
package org.ligo.patterns.impl.datapatterns;

import static java.lang.String.*;
import static java.util.Collections.*;

import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.data.LigoData;
import org.ligo.data.impl.NamedData;
import org.ligo.patterns.api.LigoPattern;
import org.ligo.patterns.impl.DefaultDataPattern;

/**
 * @author Fabio Simeoni
 *
 */
public class OneData extends DefaultDataPattern {

	/**
	 * 
	 */
	public OneData(QName name, LigoPattern pattern) {
		super(name,pattern);
	}
	
	/**{@inheritDoc}*/
	public List<NamedData> bind(List<LigoData> data) {
		
		//cardinality check
		if (data.size()!=1) 
			throw new RuntimeException("expected one "+name()+", found "+data.size());
		
		//dispatch to target predicate
		LigoData match = pattern().bind(data.get(0));
		
		return singletonList(new NamedData(name(),match));
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(super.toString(),"one");
	}
}
