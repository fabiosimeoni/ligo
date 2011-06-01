package org.ligo.core.data;

import java.util.List;

import javax.xml.namespace.QName;

public interface LigoObject extends LigoData{

	List<LigoProvider> get(QName name);
}