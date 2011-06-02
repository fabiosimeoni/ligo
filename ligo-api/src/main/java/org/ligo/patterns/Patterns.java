/**
 * 
 */
package org.ligo.patterns;

import static java.util.Arrays.*;

import java.util.LinkedList;

import javax.xml.namespace.QName;

import org.ligo.patterns.constraints.AnyValue;
import org.ligo.patterns.edgepatterns.OneData;
import org.ligo.patterns.edgepatterns.OptionalData;
import org.ligo.patterns.edgepatterns.RepeatedData;
import org.ligo.patterns.objectpatterns.AnyData;
import org.ligo.patterns.objectpatterns.IntegerPattern;
import org.ligo.patterns.objectpatterns.ObjectPattern;
import org.ligo.patterns.objectpatterns.StringPattern;

/**
 * @author Fabio Simeoni
 *
 */
public class Patterns {

	public static AnyData any = AnyData.INSTANCE;
	
	public static ObjectPattern object = new ObjectPattern(new LinkedList<DataPattern>());
	
	public static ObjectPattern object(DataPattern ... ps) {
		return new ObjectPattern(asList(ps));
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
