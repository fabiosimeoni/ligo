package org.ligo.nodes.bindings;

import org.ligo.core.annotations.Bind;


class GenericType<A> {
	
	void set(@Bind("p1") A a) {
		System.out.println(this.getClass().getSimpleName()+" invoked with "+a);
	};
}