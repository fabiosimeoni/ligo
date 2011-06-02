package org.ligo.data;

import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

public interface LigoObject extends LigoData{

	List<LigoData> data(QName name);
	Set<QName> names();
}