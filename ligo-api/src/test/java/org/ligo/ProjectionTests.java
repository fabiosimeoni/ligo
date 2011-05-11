/**
 * 
 */
package org.ligo;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.ligo.api.Binder;
import org.ligo.api.Pattern;
import org.ligo.api.PatternFactory;
import org.ligo.testapp.Managed;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni (University of Strathclyde)
 * 
 */
public class ProjectionTests {

	static Logger logger = LoggerFactory.getLogger(ProjectionTests.class);

	@Test
	@SuppressWarnings("unchecked")
	public void project() {

		final String expected = "expected";
		
		final Managed m = new Managed();

		Pattern<Void,Void> pattern = mock(Pattern.class);
		
		PatternFactory<Void,Void> generator = mock(PatternFactory.class);
		when(generator.generate(any(Class.class))).thenReturn(pattern); 
		
		Binder<Void> binder = mock(Binder.class);
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) throws Throwable {
				m.setS(expected);
				return null;
			}
		}).when(binder).bind(any(Class.class),any(Void.class));
		
		
//		Projector p = new DefaultProjector<Void,Void>(generator,binder);
//
//		p.project(Managed.class, null);

		assertEquals(expected, m.getS());

	}
}
