/**
 * 
 */
package org.ligo.core.impl;

import static java.lang.String.*;
import static org.ligo.core.BindMode.*;
import static org.ligo.core.keys.Keys.*;
import static org.ligo.core.utils.ReflectionUtils.*;

import java.util.List;

import org.ligo.core.Environment;
import org.ligo.core.TypeBinder;
import org.ligo.core.data.LigoData;
import org.ligo.core.data.LigoProvider;
import org.ligo.core.data.LigoValue;
import org.ligo.core.keys.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Fabio Simeoni
 *
 */
class PrimitiveBinder<TYPE> extends AbstractBinder<TYPE> {
	
	private static Logger logger= LoggerFactory.getLogger(PrimitiveBinder.class);
	
	static final String TO_STRING= "%1s-%2s";
	static String CARDINALITY_ERROR = "%1s required one value but found: %2s";
	static String INPUT_ERROR = "%1s required a scalar but found: %2s";

	
	public PrimitiveBinder(Key<TYPE> key) {
		super(key);
	}
	
	public TYPE bind(List<LigoProvider> providers) {
		
		TYPE defaultValue = (TYPE) defaultOf(key().toClass());
		
		if (providers.size()!=1)
			if (mode()==STRICT)
				throw new RuntimeException(format(CARDINALITY_ERROR,this,providers));
			else {
				logger.trace(BINDING_SUCCESS_LOG,new Object[]{providers,this,defaultValue});
				return defaultValue;
			}
		
		LigoData data = providers.get(0).provide();
	
		if (!(data instanceof LigoValue))
			if (mode()==STRICT)
					throw new RuntimeException(format(INPUT_ERROR,this,providers));
			else {
				logger.trace(BINDING_SUCCESS_LOG,new Object[]{data,this,defaultValue});
				return defaultValue;
			}
	
		LigoValue ligoValue = (LigoValue) data;
		
		Object javaObject = ligoValue.get();
		
		
		Object javaValue=null;
		Throwable error=null;
		
		try{
			javaValue = key().toClass().isAssignableFrom(javaObject.getClass())? 
							key().toClass().cast(javaObject) : 
							valueOf(key().toClass(),javaObject.toString());
		}
		catch(Throwable t) {
			error=t;
		}
		
		if (javaValue == null)
			if (mode()==STRICT)
				throw error==null?
					new RuntimeException(format(BINDING_ERROR,javaObject,this)):
					new RuntimeException(format(BINDING_ERROR,javaObject,this),error);
			else
				javaValue = defaultValue;
			
		@SuppressWarnings("unchecked")
		TYPE result = (TYPE) javaValue;
		
		logger.trace(BINDING_SUCCESS_LOG,new Object[]{data,this,result});
		
		return result;
	};
	

	static public <TYPE> BinderProvider<TYPE> provider(final Class<TYPE> clazz) {
		
		//logger.trace(BUILT_LOG,PrimitiveBinder.this,mode());
		return new BinderProvider<TYPE>() {
			
			@Override public Key<? extends TYPE> matchingKey() {
				return newKey(clazz);
			}
			
			@Override
			public TypeBinder<TYPE> binder(Key<TYPE> key, Environment factory) {
				return new PrimitiveBinder<TYPE>(key); 
			}
		};
	}
	


	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(TO_STRING,super.toString(),key());
	}
}
