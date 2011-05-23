package org.ligo.lab.data;

import javax.xml.namespace.QName;

public interface StructureProvider extends DataProvider{

	DataProvider[] get(QName name);
}