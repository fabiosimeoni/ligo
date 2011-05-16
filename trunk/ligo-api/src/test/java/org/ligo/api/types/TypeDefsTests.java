/**
 * 
 */
package org.ligo.api.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.ligo.api.data.DataProvider;
import org.ligo.api.data.StructureProvider;
import org.ligo.api.data.ValueProvider;
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
		ofactory.addBinding(ManagedDep.class,DepImpl.class);
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
		Map<String,Object> paramdepdata = new HashMap<String, Object>();
		paramdepdata.put("p1", "hello");
		data.put("p6",paramdepdata);
		
		def.newInstance(toProvider(data));
		
		System.out.println(def);
				
	}
	
	DataProvider[] toProvider(Object o) {
		
		if (o instanceof Map<?,?>) {
			return sp((Map) o);
		}
		if (o instanceof Object[]) {
			return ap((Object[]) o);
		}
		return vp(o);
	}
	
	private DataProvider[] sp(final Map<String,Object> map) {
		
		return new StructureProvider[]{new StructureProvider() {

			/**{@inheritDoc}*/
			@Override
			public DataProvider[] get(QName name) {
				return toProvider(map.get(name.getLocalPart()));			
			}
			
			/**{@inheritDoc}*/
			@Override
			public String toString() {
				return map.toString();
			}
		}};
	}
	
	private DataProvider[] ap(final Object[] map) {
		List<DataProvider> dps = new ArrayList<DataProvider>();
		
		for (Object o : map)
			dps.add(toProvider(o)[0]);
		
		return dps.toArray(new DataProvider[0]);
	}
	
	private ValueProvider[] vp(final Object val)  {
		return new ValueProvider[]{ new ValueProvider() {
			
			public <T> T get(Class<T> expected) {
				return expected.equals(String.class)?(T)val.toString():null;
			}
			
			/**{@inheritDoc}*/
			@Override
			public String toString() {
				return val.toString();
			}
		}};
	}
}

