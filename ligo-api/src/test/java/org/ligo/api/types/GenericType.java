package org.ligo.api.types;

import org.ligo.api.Project;


class GenericType<A> {
	
	void set(@Project("p1") A a) {};
}