/**
 * 
 */
package org.ligo.api.types;

import org.junit.Test;
import org.ligo.api.types.api.TypeDef;
import org.ligo.api.types.api.TypeDefFactory;
import org.ligo.api.types.impl.DefaultTypeDefFactory;

/**
 * @author Fabio Simeoni
 *
 */
public class TypeDefFactoryTests {

	@Test
	public void generate() {
		
		
		TypeDefFactory factory = new DefaultTypeDefFactory();
		
		factory.register(ManagedDep.class,DepImpl.class);
		
		TypeDef<Managed> def = factory.generate(Managed.class);
		
		System.out.println(def);
	}
}
