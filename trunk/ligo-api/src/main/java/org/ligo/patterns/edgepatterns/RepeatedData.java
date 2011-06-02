/**
 * 
 */
package org.ligo.patterns.edgepatterns;

import static java.lang.String.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.core.data.LigoData;
import org.ligo.core.data.impl.NamedData;
import org.ligo.patterns.LigoPattern;
import org.ligo.patterns.DataPattern;

/**
 * @author Fabio Simeoni
 *
 */
public class RepeatedData extends DataPattern {

	/**
	 * 
	 */
	public RepeatedData(QName name, LigoPattern pattern) {
		super(name,pattern);
	}
	
	/**{@inheritDoc}*/
	public List<NamedData> bind(List<LigoData> data) {
		
		List<NamedData> matches = new ArrayList<NamedData>();
		
		for (LigoData d : data)
			try {
				//dispatch to target predicate
				LigoData match = pattern().bind(d);
				matches.add(new NamedData(name(),match));
				
			} catch(Exception tolerate){}
			
		return matches;
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(super.toString(),"many");
	}
}
