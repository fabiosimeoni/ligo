/**
 * 
 */
package org.ligo.data.impl;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.ligo.data.LigoData;
import org.ligo.data.LigoObject;

/**
 * @author Fabio Simeoni
 *
 */
public class DefaultLigoObject extends AbstractLigoObject implements LigoObject {
	
	//preserving insertion order
	final Map<QName,List<LigoData>> dataMap = new HashMap<QName,List<LigoData>>();

	public DefaultLigoObject(List<NamedData> data) {
		for (NamedData named : data) {
			List<LigoData> existing = dataMap.get(named.name());
			if (existing==null) {
				existing = new ArrayList<LigoData>();
				dataMap.put(named.name(), existing);
			}
			existing.add(named.data());
		}
	}
	
	/**{@inheritDoc}*/
	@Override
	public List<LigoData> data(QName name) {
		List<LigoData> data = dataMap.get(name);
		return data==null? Collections.<LigoData>emptyList():unmodifiableList(data);
	}
	
	/**{@inheritDoc}*/
	@Override
	public Set<QName> names() {
		return unmodifiableSet(dataMap.keySet());
	}

	
}
