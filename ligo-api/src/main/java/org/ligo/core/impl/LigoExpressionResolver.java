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
import org.ligo.data.LigoData;
import org.ligo.data.LigoObject;

/**
 * @author Fabio Simeoni
 *
 */
public class LigoExpressionResolver implements ExpressionResolver {

	private static final Pattern REGEXP_PATTERN = Pattern.compile("^\\((.*)\\)$");

	/**{@inheritDoc}*/
	@Override
	public List<LigoData> resolve(QName exp, LigoObject ligoObject) {
		
		String[] names = exp.getLocalPart().split("/");
		List<LigoData> data = singletonList((LigoData)ligoObject);
		
		for (String name : names) {
			List<LigoData> results = new ArrayList<LigoData>();
			for (LigoData d : data) {
				if (d instanceof LigoObject)
					  results.addAll(match(new QName(exp.getNamespaceURI(),name),(LigoObject) d));
			}
			data=results;
		}
		
		return data;
	}

	List<LigoData> match(QName name, LigoObject object) {
		
		List<LigoData> matches = new ArrayList<LigoData>();
		Matcher matcher = REGEXP_PATTERN.matcher(name.getLocalPart());
		
		if (matcher.matches()) {//regexp case
			String regexp = matcher.group(1);
			for (QName n : object.names())
				if (n.getLocalPart().matches(regexp))
					matches.addAll(object.data(n));
		}
		else 
			matches = object.data(name);
		
		return matches;
	}
}
