/**
 * 
 */
package org.ligo.core;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.ligo.core.utils.Constants.*;
import static org.ligo.data.impl.DataBuilders.*;

import java.util.List;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.ligo.core.binders.api.Environment;
import org.ligo.core.binders.impl.LigoEnvironment;
import org.ligo.core.resolvers.impl.LigoExpressionResolver;
import org.ligo.core.resolvers.impl.LigoResolver;
import org.ligo.data.LigoData;
import org.ligo.data.LigoObject;

/**
 * @author Fabio Simeoni
 *
 */
public class ExpressionResolverTests {

	LigoResolver resolver;
	Environment env;
	
	@Before
	public void setup() {

		resolver = new LigoResolver();
		env = new LigoEnvironment(resolver);
	}
	
	@Test
	public void resolve() {
		
		LigoObject lo = o(
							n("1",
								o(n("2",
										o(n("3",1))))),
							n("1",
								o(n("2",
										o(n("3",2))))),
							n("1","stopshort"),
							n("1",o(n("2","stopshort"))),
							n("other",2));
		
		LigoExpressionResolver r = new LigoExpressionResolver();
		
		List<? extends LigoData> ps =  r.resolve(new QName("1/2/3"), lo);
		assertEquals(d(v(1),v(2)), ps);
		
		ps =  r.resolve(new QName("1/(.*)"), lo);
		assertEquals(3,ps.size());
		
		ps =  r.resolve(new QName("(.*)"), lo);
		assertEquals(5,ps.size());
		
		ps =  r.resolve(new QName("1"), lo);
		assertEquals(4,ps.size());
		
	}
	
	@Test
	public void groups() {
		
		LigoObject lo = o(
							n("a",o(
									n("b",1),
									n("b",2))),
							n("a",o(
									n("b",3),
									n("b",4))));
		
		LigoExpressionResolver r = new LigoExpressionResolver();
		
		List<? extends LigoData> ps =  r.resolve(new QName("a[b]"), lo);
		List<LigoObject> expected = asList(
				o(n(NONAME,1),n(NONAME,2)),
				o(n(NONAME,3),n(NONAME,4)));
		assertEquals(expected, ps);
		
		
	}

}

