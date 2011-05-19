package org.ligo.lab.types.api;

public interface TypeDefConstructor<TYPE> {
	
	TypeDef<TYPE> getTypeDef(TypeKey<TYPE> key,TypeDefFactory factory);
	
}