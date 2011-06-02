package org.ligo.core.data;

import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

public interface LigoObject extends LigoData{

	List<LigoData> get(QName name);
	Set<QName> names();
}