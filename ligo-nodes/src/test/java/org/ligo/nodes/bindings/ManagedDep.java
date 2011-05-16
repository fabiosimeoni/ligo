package org.ligo.nodes.bindings;

import org.ligo.api.Project;

interface ManagedDep {
	
	void setInteger(@Project("p1") Integer i);
}