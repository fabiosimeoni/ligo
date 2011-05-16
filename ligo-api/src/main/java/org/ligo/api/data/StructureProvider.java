package org.ligo.api.data;

import javax.xml.namespace.QName;

public interface StructureProvider extends DataProvider{

	DataProvider[] get(QName name);
}