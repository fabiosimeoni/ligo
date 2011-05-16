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
import org.ligo.api.types.impl.SimpleObjectFactory;

/**
 * @author Fabio Simeoni
 *
 */
public class TypeDefsTests {

	
	@Test
	public void bind() {
		
		
		SimpleObjectFactory ofactory = new SimpleObjectFactory();
		ofactory.register(ManagedDep.class,DepImpl.class);
		TypeDefFactory factory = new DefaultTypeDefFactory(ofactory);
		
		
		TypeDef<Managed> def = factory.getTypeDef(Managed.class);
		
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("p1","hello");
		data.put("p2","world");
		Map<String,Object> depdata = new HashMap<String, Object>();
		depdata.put("p1",10);
		data.put("p3",depdata);
		data.put("p4",new String[]{"hello","world"});
		data.put("p5",new String[]{"hello","world"});
		
		def.newInstance(data);
		
		System.out.println(def);
				
	}
	
	
}

