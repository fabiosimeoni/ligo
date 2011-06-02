/**
 * 
 */
package org.ligo.core;

import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.core.data.LigoData;
import org.ligo.core.data.LigoObject;

/**
 * @author Fabio Simeoni
 *
 */
public interface ExpressionResolver {

	
	List<LigoData> resolve(QName exp, LigoObject provider);
}
