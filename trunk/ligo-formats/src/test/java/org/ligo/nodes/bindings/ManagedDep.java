package org.ligo.nodes.bindings;

import org.ligo.core.annotations.Bind;

interface ManagedDep {
	
	void setInteger(@Bind("p1") Integer i);
}