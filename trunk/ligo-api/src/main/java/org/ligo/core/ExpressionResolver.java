/**
 * 
 */
package org.ligo.core;

import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.core.data.LigoProvider;

/**
 * @author Fabio Simeoni
 *
 */
public interface ExpressionResolver {

	
	List<LigoProvider> resolve(QName exp, LigoProvider provider);
}
