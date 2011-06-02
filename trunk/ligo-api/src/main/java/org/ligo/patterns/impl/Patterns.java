/**
 * 
 */
package org.ligo.patterns.impl;

import static java.util.Arrays.*;

import java.util.LinkedList;

import javax.xml.namespace.QName;

import org.ligo.patterns.api.LigoPattern;
import org.ligo.patterns.api.ObjectPattern;
import org.ligo.patterns.impl.constraints.AnyValue;
import org.ligo.patterns.impl.datapatterns.OneData;
import org.ligo.patterns.impl.datapatterns.OptionalData;
import org.ligo.patterns.impl.datapatterns.RepeatedData;
import org.ligo.patterns.impl.objectpatterns.AnyData;
import org.ligo.patterns.impl.objectpatterns.IntegerPattern;
import org.ligo.patterns.impl.objectpatterns.StringPattern;

/**
 * @author Fabio Simeoni
 *
 */
public class Patterns {

	public static AnyData any = AnyData.INSTANCE;
	
	public static ObjectPattern object = new DefaultObjectPattern(new LinkedList<DefaultDataPattern>());
	
	public static ObjectPattern object(DefaultDataPattern ... ps) {
		return new DefaultObjectPattern(asList(ps));
	}
	
	
	
	public static OneData one(String l,LigoPattern p) {
		return new OneData(new QName(l),p);
	}
	
	public static OneData one(QName l, LigoPattern p) {
		return new OneData(l,p);
	}
	
	public static OptionalData opt(String l,LigoPattern p) {
		return new OptionalData(new QName(l),p);
	}
	
	public static OptionalData opt(QName l, LigoPattern p) {
		return new OptionalData(l,p);
	}
	
	public static RepeatedData many(String l,LigoPattern p) {
		return new RepeatedData(new QName(l),p);
	}
	
	public static RepeatedData many(QName l, LigoPattern p) {
		return new RepeatedData(l,p);
	}
	
	public static StringPattern string = new StringPattern(AnyValue.INSTANCE);
	public static IntegerPattern integer = new IntegerPattern(AnyValue.INSTANCE);
}
