/**
 * 
 */
package org.ligo.core.resolvers.api;

import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.data.LigoData;

/**
 * @author Fabio Simeoni
 *
 */
public interface ExpressionResolver {

	
	List<? extends LigoData> resolve(QName exp, LigoData data);
}
