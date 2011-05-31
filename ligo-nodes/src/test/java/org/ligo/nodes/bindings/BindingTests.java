/**
 * 
 */
package org.ligo.nodes.bindings;

import static org.ligo.nodes.model.impl.Nodes.*;

import org.junit.Test;
import org.ligo.core.TypeBinder;
import org.ligo.core.impl.LigoEnvironment;
import org.ligo.core.impl.LigoResolver;
import org.ligo.nodes.model.api.Node;

/**
 * @author Fabio Simeoni
 *
 */
public class BindingTests {

	@Test
	public void bind() {
		
		LigoResolver resolver = new LigoResolver();
		resolver.bind(ManagedDep.class, DepImpl.class);
		LigoEnvironment env = new LigoEnvironment(resolver);
	
		TypeBinder<Managed> binder = env.binderFor(Managed.class);
		
		Node n = n(
				e("p1","hello"),
				e("p2","world"),
				e("p3",n(e("p1",10))),
				e("p4","hello"),
				e("p4","world"),
				e("p5","hello"),
				e("p5","world"),
				e("p6",n(e("p1","hello")))
		);
		
		binder.bind(n);
	}
		
}
