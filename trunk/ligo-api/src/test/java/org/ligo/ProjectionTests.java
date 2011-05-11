/**
 * 
 */
package org.ligo;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.ligo.api.Binder;
import org.ligo.api.DefaultProjector;
import org.ligo.api.Pattern;
import org.ligo.api.PatternFactory;
import org.ligo.api.Projector;
import org.ligo.api.configuration.LigoContext;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Simeoni (University of Strathclyde)
 * 
 */
@SuppressWarnings("unchecked")
public class ProjectionTests {

	static Logger logger = LoggerFactory.getLogger(ProjectionTests.class);
	
	@Test
	public void project() {

		final String expected = "expected";
		
		final Managed m = new Managed();

		Binder<VoidModel> binder = mockBinder(new Answer<VoidModel>() {
			public VoidModel answer(InvocationOnMock invocation) throws Throwable {
				m.setS(expected);
				return null;
			}
		});
		
		Projector p = new DefaultProjector<VoidModel,VoidModel>(mockContext(binder,mockFactory()));

		p.project(Managed.class, new VoidModel());

		assertEquals(expected, m.getS());

	}
	
	static class VoidModel {};
	
	Binder<VoidModel> mockBinder(Answer<VoidModel> answer) {
		
		Binder<VoidModel> binder = mock(Binder.class);
		doAnswer(answer).when(binder).bind(any(Class.class),any(VoidModel.class));
		return binder;
	}
	
	PatternFactory<VoidModel,VoidModel> mockFactory() {
		
		Pattern<VoidModel,VoidModel> pattern = mock(Pattern.class);
		when(pattern.extract(any(VoidModel.class))).thenReturn(new VoidModel());
		
		PatternFactory<VoidModel,VoidModel> factory = mock(PatternFactory.class);
		when(factory.generate(any(Class.class))).thenReturn(pattern); 
		
		return factory;
	}
	
	LigoContext<VoidModel,VoidModel> 
		mockContext(final Binder<VoidModel> b, final PatternFactory<VoidModel,VoidModel> f) {
		
		return new LigoContext<VoidModel,VoidModel>() {

			public Binder<VoidModel> binder() {
				return b;
			}

			public Class<? extends VoidModel> matchType() {
				return VoidModel.class;
			}

			public Class<? extends VoidModel> modelType() {
				return VoidModel.class;
			}

			@Override
			public PatternFactory<VoidModel, VoidModel> patternFactory() {
				return f;
			}
		};
		
	}
	
	public static class Managed {

		String s;
		
		public void setS(String s) {
			this.s = s;
		}
		
		public String getS() {
			return s;
		}
		
	}
}
