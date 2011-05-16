package org.ligo.nodes.bindings;

import org.ligo.api.Project;


class GenericType<A> {
	
	void set(@Project("p1") A a) {
		System.out.println(this.getClass().getSimpleName()+" invoked with "+a);
	};
}