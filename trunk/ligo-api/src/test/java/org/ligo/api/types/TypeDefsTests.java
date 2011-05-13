/**
 * 
 */
package org.ligo.api.types;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.ligo.api.types.api.TypeDef;
import org.ligo.api.types.api.TypeDefFactory;
import org.ligo.api.types.impl.DefaultTypeDefFactory;

/**
 * @author Fabio Simeoni
 *
 */
public class TypeDefsTests {

	
	@Test
	public void bind() {
		
		
		TypeDefFactory factory = new DefaultTypeDefFactory();
		factory.register(ManagedDep.class,DepImpl.class);
		
		
		TypeDef<Managed> def = factory.generate(Managed.class);
		
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("p1","hello");
		data.put("p2","world");
		Map<String,Object> depdata = new HashMap<String, Object>();
		depdata.put("p1",10);
		data.put("p3",depdata);
		
		def.newInstance(data);
		
		System.out.println(def);
				
	}
	
	
}

