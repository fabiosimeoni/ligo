/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static java.lang.String.*;
import static org.ligo.lab.typebinders.Bind.Mode.*;
import static org.ligo.lab.typebinders.Key.*;
import static org.ligo.lab.typebinders.kinds.Kind.*;
import static org.ligo.lab.typebinders.utils.ReflectionUtils.*;

import java.util.List;

import org.ligo.lab.data.DataProvider;
import org.ligo.lab.data.Provided;
import org.ligo.lab.data.ValueProvider;
import org.ligo.lab.typebinders.Environment;
import org.ligo.lab.typebinders.Key;
import org.ligo.lab.typebinders.TypeBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Fabio Simeoni
 *
 */
public class PrimitiveBinder<TYPE> extends AbstractBinder<TYPE> {
	
	private static Logger logger= LoggerFactory.getLogger(PrimitiveBinder.class);
	
	private static String CARDINALITY_ERROR = "[%1s] binder for %2s required one value but found: %3s";
	private static String INPUT_ERROR = "[%1s] binder for %2s required a scalar but found: %3s";
	
	public PrimitiveBinder(Class<TYPE> c) {
		super(get(c));
	}
	
	public TYPE bind(List<Provided> provided) {
		
		Class<?> clazz = CLASS(key().kind());
		
		@SuppressWarnings("unchecked")
		TYPE defaultValue = (TYPE) defaultOf(clazz);
		
		if (provided.size()!=1)
			if (mode()==STRICT)
				throw new RuntimeException(format(CARDINALITY_ERROR,mode(),this,provided));
			else {
				logger.trace(BINDING_SUCCESS_LOG,new Object[]{mode(),provided,this,defaultValue});
				return defaultValue;
			}
		
		DataProvider dp = provided.get(0).provider();
	
		if (!(dp instanceof ValueProvider))
			if (mode()==STRICT)
					throw new RuntimeException(format(INPUT_ERROR,mode(),this,provided));
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
					new RuntimeException(format(BINDING_ERROR,mode(),input,this)):
					new RuntimeException(format(BINDING_ERROR,mode(),input,this),error);
			else
				output = defaultValue;
			
		@SuppressWarnings("unchecked")
		TYPE result = (TYPE) output;
		
		logger.trace(BINDING_SUCCESS_LOG,new Object[]{mode(),dp,this,result});
		
		return result;
	};
	

	static public <TYPE> BinderProvider<TYPE> provider(final Class<TYPE> clazz) {
		
		//logger.trace(BUILT_LOG,PrimitiveBinder.this,mode());
		return new BinderProvider<TYPE>() {
			
			@Override public Key<? extends TYPE> matchingKey() {
				return get(clazz);
			}
			
			@Override
			public TypeBinder<TYPE> binder(Key<TYPE> key, Environment factory) {
				return new PrimitiveBinder<TYPE>(clazz); 
			}
		};
	}

	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return CLASS(key().kind()).getSimpleName().toLowerCase();
	}
}
