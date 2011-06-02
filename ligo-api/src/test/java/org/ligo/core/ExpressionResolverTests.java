/**
 * 
 */
package org.ligo.core;

import static org.junit.Assert.*;
import static org.ligo.core.TestData.*;

import java.util.List;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.ligo.core.data.LigoProvider;
import org.ligo.core.impl.LigoEnvironment;
import org.ligo.core.impl.LigoExpressionResolver;
import org.ligo.core.impl.LigoResolver;

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
		
		LigoProvider prov = s(
								p("1",
									s(p("2",
											s(p("3",1))))),
								p("1",
									s(p("2",
											s(p("3",2))))),
								p("1","stopshort"),
								p("1",s(p("2","stopshort"))),
								p("other",2));
		
		LigoExpressionResolver r = new LigoExpressionResolver();
		
		List<LigoProvider> ps =  r.resolve(new QName("1/2/3"), prov);
		assertEquals(list(v(1),v(2)), ps);
		
		ps =  r.resolve(new QName("1/(.*)"), prov);
		assertEquals(3,ps.size());
		
		ps =  r.resolve(new QName("(.*)"), prov);
		assertEquals(5,ps.size());
		
		ps =  r.resolve(new QName("1"), prov);
		assertEquals(4,ps.size());
		
	}

}

