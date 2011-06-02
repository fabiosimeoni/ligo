/**
 * 
 */
package org.ligo.patterns.api;

import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.data.LigoData;
import org.ligo.data.impl.NamedData;

/**
 * @author Fabio Simeoni
 *
 */
public interface DataPattern {

	/**
	 * @return the name
	 */
	QName name();

	/**
	 * @return the pattern
	 */
	LigoPattern pattern();

	List<NamedData> bind(List<LigoData> data);

}