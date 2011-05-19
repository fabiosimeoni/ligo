/**
 * 
 */
package org.ligo.lab.types.impl;

import static java.lang.String.*;
import static java.util.Arrays.*;

import org.ligo.lab.data.DataProvider;
import org.ligo.lab.data.ValueProvider;
import org.ligo.lab.types.api.TypeKey;


/**
 * @author Fabio Simeoni
 *
 */
public class PrimitiveTypeDef<TYPE> extends AbstractTypeDef<TYPE> {
	
	public PrimitiveTypeDef(TypeKey<TYPE> key) {
		super(key);
	}
	
	public TYPE bind(DataProvider[] providers) {
		
		try {
			
			if (providers.length==0)
				return null;
			
			if (providers.length>1)
				throw new RuntimeException("expected one value but found many: "+asList(providers));
			
			if (!(providers[0] instanceof ValueProvider))
				throw new RuntimeException("expected a value but found "+providers[0]);
			
			ValueProvider vp = (ValueProvider) providers[0];
			
			return vp.get(key().type());
		}
		catch(ClassCastException e) {
			throw new RuntimeException(format("cannot bind %1s to %2s",key().type(),asList(providers)),e);
		}
	};

}