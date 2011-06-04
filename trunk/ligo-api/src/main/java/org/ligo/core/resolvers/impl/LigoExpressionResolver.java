/**
 * 
 */
package org.ligo.core.resolvers.impl;

import static java.util.Collections.*;
import static org.ligo.core.utils.Constants.*;
import static org.ligo.data.impl.DataBuilders.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.ligo.core.resolvers.api.ExpressionResolver;
import org.ligo.data.LigoData;
import org.ligo.data.LigoObject;
import org.ligo.data.LigoValue;
import org.ligo.data.impl.NamedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni
 *
 */
public class LigoExpressionResolver implements ExpressionResolver {

	
	private static final Logger logger = LoggerFactory.getLogger(LigoExpressionResolver.class);
	
	private static final Pattern REGEXP_PATTERN = Pattern.compile("^\\((.*)\\)$");
	private static final Pattern COLLNODE_PATTERN = Pattern.compile("^([^\\[]*)\\[(.*)\\]$");
	private static final Pattern IMPLICIT_ELEMENT_PATTERN = Pattern.compile("^\\[(.*)\\]$");
	private static final String COLLNODE_CREATION_LOG = "converted {} into collection node {}";

	/**{@inheritDoc}*/
	@Override
	public List<? extends LigoData> resolve(QName exp, LigoData data) {
		
		String[] names = exp.getLocalPart().split("/");
		List<QName> path = new ArrayList<QName>();
		for (String name : names) 
			path.add(new QName(exp.getNamespaceURI(),name));
			
		return resolve(path,data);
	}
	
	
	List<? extends LigoData> resolve(List<QName> path, LigoData data) {
		
		//empty path
		if (path.isEmpty())
			return singletonList(data);
		
		//non-empty path
		List<LigoData> resolved = new ArrayList<LigoData>();
		
		//value case: path can no longer be resolved
		if (data instanceof LigoValue)
			return resolved;
		
		//object case
		LigoObject object = (LigoObject) data;
		
		//get first name on path
		QName currentName = path.get(0);
		
		//process grouping, if any (e.g. people[person])
		String unqualified = currentName.getLocalPart();
		Matcher matcher = COLLNODE_PATTERN.matcher(unqualified);
		if (matcher.matches()) {
			
			//extract collection expression (e.g. people[person]->people)
			String collExpression = matcher.group(1);
			currentName = new QName(currentName.getNamespaceURI(),collExpression);
			
			//extract element expression (e.g. people[person]->people)
			String  elementExpression = matcher.group(2); 
			if (elementExpression.isEmpty())
				elementExpression = "(.*)";
			else {
				Matcher implicitElMatcher = IMPLICIT_ELEMENT_PATTERN.matcher(elementExpression);
				if (implicitElMatcher.matches())
					elementExpression = "(.*)"+implicitElMatcher.group(0);
			}
				
			QName elementPath = new QName(currentName.getNamespaceURI(),elementExpression);
			
			//System.out.println(currentName+" matches "+match(currentName,object));
			
			//for each 
			for (LigoData match : match(currentName,object)) {
				List<NamedData> elements = new ArrayList<NamedData>();
				//System.out.println("resolving "+elementPath+" on "+match);
				for (LigoData element : resolve(elementPath,match))
					elements.add(n(NONAME,element));
				LigoObject collection = o(elements.toArray(new NamedData[0])); 
				resolved.add(collection);
				logger.trace(COLLNODE_CREATION_LOG,object,collection);
			}
			
					
		} 
		else
			for (LigoData match : match(currentName,object))
				resolved.addAll(resolve(path.subList(1, path.size()),match));
		
		return resolved;
	}
	
	List<LigoData> match(QName name, LigoObject object) {
		
		List<LigoData> matches = new ArrayList<LigoData>();
		
		String unqualified = name.getLocalPart();
		
		Matcher matcher = REGEXP_PATTERN.matcher(unqualified);
		
		if (matcher.matches()) {//regexp case
			String regexp = matcher.group(1);
			for (QName n : object.names())
				if (unqualified.matches(regexp))
					matches.addAll(object.data(n));
		}
		else 
			matches = object.data(name);

				
		return matches;
	}
}
