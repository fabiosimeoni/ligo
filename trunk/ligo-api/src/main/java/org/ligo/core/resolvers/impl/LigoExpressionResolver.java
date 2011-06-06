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
 * 
 * 	  LBL = l | (l) | epsilon
 *    PATH = LBL  | PATH/EXP 
*  	  EXP = EXP [EXP] | PATH
 * 
 * @author Fabio Simeoni
 *
 */
public class LigoExpressionResolver implements ExpressionResolver {

	private static final Logger logger = LoggerFactory.getLogger(LigoExpressionResolver.class);
	
	static final Pattern COLLEXP_PATTERN = Pattern.compile("^([^\\[]*)\\[(.*)\\]$");
	static final Pattern PATHEXP_PATTERN = Pattern.compile("^([^/\\[]+)$|^([^/\\[]+)/(.+)$");
	static final Pattern REGEXP_PATTERN = Pattern.compile("^\\((.*)\\)$");
	static final String INVALID_EXPRESSION_ERROR = "malformed expression %1";
	static final String COLLNODE_CREATION_LOG = "converted {} into collection node {}";

	private static final QName emptyExp = new QName("");
	
	/**{@inheritDoc}*/
	@Override
	public List<? extends LigoData> resolve(QName exp, LigoData data) {
		
		String unqualified = exp.getLocalPart();
		
		//is it a coll expression?
		Matcher m = COLLEXP_PATTERN.matcher(unqualified);
		
		if (m.matches()) {
			QName collExp = new QName(exp.getNamespaceURI(),m.group(1));
			QName elementExp = new QName(exp.getNamespaceURI(),m.group(2));
			return resolveCollection(collExp,elementExp, data);
		
		}
		// is it a path expression
		m= PATHEXP_PATTERN.matcher(unqualified);
		if (m.matches())
			//is it a plain name?
			if (m.group(1)!=null)
				return resolvePath(exp,emptyExp,data);
			//it is a full path
			else {//path case
				QName name = new QName(exp.getNamespaceURI(),m.group(2));
				QName rest = new QName(exp.getNamespaceURI(),m.group(3));
				return resolvePath(name,rest,data);
			}
				
		//it is an malformed expression
		throw new RuntimeException(String.format(INVALID_EXPRESSION_ERROR,unqualified));
	}
	
	
	
	List<? extends LigoData> resolveCollection(QName collExp, QName elementExp, LigoData data) {
		
		if (collExp.getLocalPart().isEmpty())
			collExp = new QName(collExp.getNamespaceURI(),"(.*)");
		
		if (elementExp.getLocalPart().isEmpty())
			elementExp = new QName(elementExp.getNamespaceURI(),"(.*)");
		
		List<LigoData> resolved = new ArrayList<LigoData>();
		
		for (LigoData match : matchLabel(collExp,data)) {
			
			List<NamedData> elements = new ArrayList<NamedData>();
			//System.out.println("resolving "+elementPath+" on "+match);
			for (LigoData el : resolve(elementExp,match))
				elements.add(n(NONAME,el));
			
			if (!elements.isEmpty()) {
				LigoObject collection = o(elements.toArray(new NamedData[0])); 
				
				resolved.add(collection);
				
				logger.trace(COLLNODE_CREATION_LOG,match,collection);
			}
		}
		
		return resolved;
		
	}
	
	List<? extends LigoData> resolvePath(QName name, QName rest, LigoData data) {
		
		List<LigoData> matches =  matchLabel(name,data);
		
		if (rest.getLocalPart().isEmpty())
			return matches;
		
		List<LigoData> resolved = new ArrayList<LigoData>();
		
		for (LigoData match :matches)
			resolved.addAll(resolve(rest,match)); 
		
		return resolved;
		
		
		
	}
	
	List<LigoData> matchLabel(QName name, LigoData data) {
		
		if (data instanceof LigoValue)
			return emptyList();
		
		LigoObject object = (LigoObject) data;
		
		List<LigoData> matches = new ArrayList<LigoData>();
		
		
		String unqualified = name.getLocalPart();
			
		Matcher matcher = REGEXP_PATTERN.matcher(unqualified);
		
		if (matcher.matches()) {//regexp case
			String regexp = matcher.group(1);
			for (QName n : object.names())
				if (n.getNamespaceURI().equals(name.getNamespaceURI()) && n.getLocalPart().matches(regexp))
					matches.addAll(object.data(n));
		}
		else 
			matches = object.data(name);

		return matches;
	}


}
