/**
 * 
 */
package org.ligo.lab.core.impl;

import static java.lang.String.*;
import static org.ligo.lab.core.annotations.Bind.Mode.*;
import static org.ligo.lab.core.keys.Keys.*;
import static org.ligo.lab.core.kinds.Kind.*;
import static org.ligo.lab.core.utils.ReflectionUtils.*;

import java.util.List;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.data.DataProvider;
import org.ligo.lab.core.data.Provided;
import org.ligo.lab.core.data.ValueProvider;
import org.ligo.lab.core.keys.ClassKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Fabio Simeoni
 *
 */
class PrimitiveBinder<TYPE> extends AbstractBinder<TYPE> {
	
	private static Logger logger= LoggerFactory.getLogger(PrimitiveBinder.class);
	
	static final String TO_STRING= "%1s-%2s";
	private static String CARDINALITY_ERROR = "binder for %1s required one value but found: %2s";
	private static String INPUT_ERROR = "binder for %1s required a scalar but found: %2s";
	
	public PrimitiveBinder(Class<TYPE> clazz) {
		this(newKey(clazz));
	}
	
	public PrimitiveBinder(ClassKey<TYPE> key) {
		super(key);
	}
	
	public TYPE bind(List<Provided> provided) {
		
		Class<?> clazz = CLASS(key().kind());
		
		@SuppressWarnings("unchecked")
		TYPE defaultValue = (TYPE) defaultOf(clazz);
		
		if (provided.size()!=1)
			if (mode()==STRICT)
				throw new RuntimeException(format(CARDINALITY_ERROR,this,provided));
			else {
				logger.trace(BINDING_SUCCESS_LOG,new Object[]{mode(),provided,this,defaultValue});
				return defaultValue;
			}
		
		DataProvider dp = provided.get(0).provider();
	
		if (!(dp instanceof ValueProvider))
			if (mode()==STRICT)
					throw new RuntimeException(format(INPUT_ERROR,this,provided));
			else {
				logger.trace(BINDING_SUCCESS_LOG,new Object[]{mode(),dp,this,defaultValue});
				return defaultValue;
			}
	
		ValueProvider vp = (ValueProvider) dp;
		
		Object input = vp.get();
		
		
		Object output=null;
		Throwable error=null;
		
		try{
			output = clazz.isAssignableFrom(input.getClass())? clazz.cast(input) : valueOf(clazz,input.toString());
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
			
			@Override public ClassKey<? extends TYPE> matchingKey() {
				return newKey(clazz);
			}
			
			@Override
			public TypeBinder<TYPE> binder(ClassKey<TYPE> key, Environment factory) {
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
