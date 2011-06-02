/**
 * 
 */
package org.ligo.patterns.impl.datapatterns;

import static java.lang.String.*;

import java.util.ArrayList;
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
public class OptionalData extends DefaultDataPattern {

	/**
	 * 
	 */
	public OptionalData(QName name, LigoPattern p) {
		super(name,p);
	}
	
	/**{@inheritDoc}*/
	public List<NamedData> bind(List<LigoData> data) {
		
		//cardinality check
		if (data.size()>1) 
			throw new RuntimeException("expected at most one "+name()+", found "+data.size());
		
		List<NamedData> matched = new ArrayList<NamedData>();
		
		if (data.size()==1) {
			try {				
				//dispatch to target predicate
				LigoData match = pattern().bind(data.get(0));
				matched.add(new NamedData(name(),match));

			} catch(Exception tolerate) {}
		}
		
		return matched;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(super.toString(),"opt");
	}
}
