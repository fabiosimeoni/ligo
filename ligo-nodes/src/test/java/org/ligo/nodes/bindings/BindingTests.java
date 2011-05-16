/**
 * 
 */
package org.ligo.nodes.bindings;

import static org.ligo.nodes.model.impl.Nodes.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.ligo.api.types.api.TypeDef;
import org.ligo.api.types.api.TypeDefFactory;
import org.ligo.api.types.impl.DefaultTypeDefFactory;
import org.ligo.api.types.impl.SimpleObjectFactory;
import org.ligo.nodes.model.api.Node;

/**
 * @author Fabio Simeoni
 *
 */
public class BindingTests {

	@Test
	public void bind() {
		
		
		SimpleObjectFactory ofactory = new SimpleObjectFactory();
		ofactory.addBinding(ManagedDep.class,DepImpl.class);
		TypeDefFactory factory = new DefaultTypeDefFactory(ofactory);
	
		TypeDef<Managed> def = factory.getTypeDef(Managed.class);
		
		Node n = n(
				e("p1","hello"),
				e("p2","world"),
				e("p3",n(e("p1",10))),
				e("p4","hello"),
				e("p4","world"),
				e("p5","hello"),
				e("p5","world"),
				e("p6",n(e("p1","hello")))
		);
		
		def.newInstance(n.provider());
		
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("p1","hello");
		data.put("p2","world");
		Map<String,Object> depdata = new HashMap<String, Object>();
		depdata.put("p1",10);
		data.put("p3",depdata);
		data.put("p4",new String[]{"hello","world"});
		data.put("p5",new String[]{"hello","world"});
		Map<String,Object> paramdepdata = new HashMap<String, Object>();
		paramdepdata.put("p1", "hello");
		data.put("p6",paramdepdata);
	}
		
}
