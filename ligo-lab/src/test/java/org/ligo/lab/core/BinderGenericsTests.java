/**
 * 
 */
package org.ligo.lab.core;

import static java.util.Collections.*;
import static org.ligo.lab.core.TestData.*;
import static org.ligo.lab.core.keys.Keys.*;
import static org.ligo.lab.core.kinds.Kind.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ligo.lab.core.TestGenericsClassDefs.GenericField;
import org.ligo.lab.core.impl.DefaultEnvironment;
import org.ligo.lab.core.keys.Key;
import org.ligo.lab.core.kinds.Kind;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author Fabio Simeoni
 *
 */
public class BinderGenericsTests {

	DefaultEnvironment factory;
	
	@Before 
	@SuppressWarnings("unchecked")
	public void setup() {
		
		Resolver p = mock(Resolver.class);
		when(p.resolve(any(Key.class))).thenAnswer(new Answer<List<Class<?>>>() {
			public List<Class<?>> answer(InvocationOnMock invocation) throws Throwable {
				return (List)singletonList(((Key)invocation.getArguments()[0]).kind().toClass());
			}
		});
		when(p.resolve(any(Key.class),any(List.class))).thenAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Kind<?> kind = ((Key)invocation.getArguments()[0]).kind();
				Object instance = null;
				try {
					switch(kind.value()) {
						case CLASS: instance = CLASS(kind).newInstance();break;
						case GENERIC: instance = ((Class<?>) GENERIC(kind).getRawType()).newInstance();
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				return instance;
			}
		});
		
		factory = new DefaultEnvironment(p);
	}

	@Test
	public void genericField() {
		
		TypeBinder<GenericField> b1 = factory.binderFor(newKey(GenericField.class));	
		
		b1.bind(s(p("a",
					s(p("b","hello")))));
	}
}

