package org.ligo.api.types;

import org.ligo.api.Project;

interface ManagedDep {
	
	void setInteger(@Project("p1") Integer i);
}