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
	
	public TYPE bind(List<LigoProvider> provided) {
		
		TYPE defaultValue = (TYPE) defaultOf(key().toClass());
		
		if (provided.size()!=1)
			if (mode()==STRICT)
				throw new RuntimeException(format(CARDINALITY_ERROR,this,provided));
			else {
				logger.trace(BINDING_SUCCESS_LOG,new Object[]{provided,this,defaultValue});
				return defaultValue;
			}
		
		LigoData dp = provided.get(0).provide();
	
		if (!(dp instanceof LigoValue))
			if (mode()==STRICT)
					throw new RuntimeException(format(INPUT_ERROR,this,provided));
			else {
				logger.trace(BINDING_SUCCESS_LOG,new Object[]{dp,this,defaultValue});
				return defaultValue;
			}
	
		LigoValue vp = (LigoValue) dp;
		
		Object input = vp.get();
		
		
		Object output=null;
		Throwable error=null;
		
		try{
			output = key().toClass().isAssignableFrom(input.getClass())? 
							key().toClass().cast(input) : 
							valueOf(key().toClass(),input.toString());
		}
		catch(Throwable t) {
			error=t;
		}
		
		if (output == null)
			if (mode()==STRICT)
				throw error==null?
					new RuntimeException(format(BINDING_ERROR,input,this)):
					new RuntimeException(format(BINDING_ERROR,input,this),error);
			else
				output = defaultValue;
			
		@SuppressWarnings("unchecked")
		TYPE result = (TYPE) output;
		
		logger.trace(BINDING_SUCCESS_LOG,new Object[]{dp,this,result});
		
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
