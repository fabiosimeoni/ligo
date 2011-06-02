/**
 * 
 */
package org.ligo.core.impl;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.ligo.core.ExpressionResolver;
import org.ligo.core.data.LigoData;
import org.ligo.core.data.LigoObject;
import org.ligo.core.data.LigoProvider;

/**
 * @author Fabio Simeoni
 *
 */
public class LigoExpressionResolver implements ExpressionResolver {

	private static final Pattern REGEXP_PATTERN = Pattern.compile("^\\((.*)\\)$");

	/**{@inheritDoc}*/
	@Override
	public List<LigoProvider> resolve(QName exp, LigoProvider provider) {
		
		String[] names = exp.getLocalPart().split("/");
		List<LigoProvider> providers = singletonList(provider);
		
		for (String name : names) {
			List<LigoProvider> results = new ArrayList<LigoProvider>();
			for (LigoProvider p : providers) {
				LigoData data = p.provide();
				if (data instanceof LigoObject)
					  results.addAll(match(new QName(exp.getNamespaceURI(),name),(LigoObject) data));
			}
			providers=results;
		}
		
		return providers;
	}

	List<LigoProvider> match(QName name, LigoObject object) {
		
		List<LigoProvider> matches = new ArrayList<LigoProvider>();
		Matcher matcher = REGEXP_PATTERN.matcher(name.getLocalPart());
		
		if (matcher.matches()) {//regexp case
			String regexp = matcher.group(1);
			for (QName n : object.names())
				if (n.getLocalPart().matches(regexp))
					matches.addAll(object.get(n));
		}
		else 
			matches = object.get(name);
		
		return matches;
	}
}
