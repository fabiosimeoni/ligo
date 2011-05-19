package org.ligo.lab.data;

public interface ValueProvider extends DataProvider{


	<T> T get(Class<T> expected);
}
