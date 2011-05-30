/**
 * 
 */
package org.ligo.lab.core;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.ligo.lab.core.keys.Keys.*;

import org.junit.Test;
import org.ligo.lab.core.TestClassDefs.BindOnConstructor;
import org.ligo.lab.core.TestClassDefs.Empty;
import org.ligo.lab.core.TestClassDefs.Primitive;
import org.ligo.lab.core.TestClassDefs.SomeInterface;
import org.ligo.lab.core.impl.LigoResolver;

/**
 * @author Fabio Simeoni
 *
 */
public class ResolverTests {

	
	@Test
	public void bind() {
		
		LigoResolver r = new LigoResolver();
		
		r.bind(SomeInterface.class,Empty.class);
		
		assertEquals(singletonList(Empty.class), r.resolve(newKey(SomeInterface.class)));
		
		r.bind(SomeInterface.class,Primitive.class);
		
		assertEquals(asList(new Class<?>[]{Empty.class,Primitive.class}), r.resolve(newKey(SomeInterface.class)));
		
	}
	
	@Test
	public void instantiate() {
		
		LigoResolver r = new LigoResolver();
		
		Empty e = r.resolve(Empty.class,emptyList());
		
		assertNotNull(e);
	}
	
	@Test
	public void instantiate2() {
		
		LigoResolver r = new LigoResolver();
		
		BindOnConstructor e = r.resolve(BindOnConstructor.class,singletonList("hello"));
		
		assertNotNull(e);
	}
}
