/**
 * 
 */
package org.ligo.api.types;

import org.junit.Test;
import org.ligo.api.types.api.TypeDef;
import org.ligo.api.types.api.TypeDefFactory;
import org.ligo.api.types.impl.DefaultTypeDefFactory;
import org.ligo.api.types.impl.SimpleObjectFactory;

/**
 * @author Fabio Simeoni
 *
 */
public class TypeDefFactoryTests {

	@Test
	public void generate() {
		
		
		SimpleObjectFactory ofactory = new SimpleObjectFactory();
		ofactory.addBinding(ManagedDep.class,DepImpl.class);
		TypeDefFactory factory = new DefaultTypeDefFactory(ofactory);
		
		TypeDef<Managed> def = factory.getTypeDef(Managed.class);
		
		System.out.println(def);
	}
}
