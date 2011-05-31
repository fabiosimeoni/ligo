package org.ligo.core.data;

import java.util.List;

import javax.xml.namespace.QName;

public interface StructureProvider extends DataProvider{

	List<Provided> get(QName name);
}