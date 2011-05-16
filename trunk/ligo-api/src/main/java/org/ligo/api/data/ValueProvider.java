package org.ligo.api.data;

public interface ValueProvider extends DataProvider{


	<T> T get(Class<T> expected);
}
