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
public class RepeatedData extends DefaultDataPattern {

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
