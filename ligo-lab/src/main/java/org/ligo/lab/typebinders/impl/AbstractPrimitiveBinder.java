/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static java.lang.String.*;
import static org.ligo.lab.typebinders.Bind.Mode.*;

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
public abstract class AbstractPrimitiveBinder<TYPE> extends AbstractBinder<TYPE> {
	
	private static Logger logger= LoggerFactory.getLogger(AbstractPrimitiveBinder.class);
	
	private static String CARDINALITY_ERROR = "[%1s] binder for %2s required one value but found: %3s";
	private static String INPUT_ERROR = "[%1s] binder for %2s required a scalar but found: %3s";
	
	public AbstractPrimitiveBinder(Key<TYPE> c) {
		super(c);
	}
	
	public TYPE bind(List<Provided> provided) {
		
		if (provided.size()!=1)
			if (mode()==STRICT)
				throw new RuntimeException(format(CARDINALITY_ERROR,mode(),this,provided));
			else {
				logger.trace(BINDING_SUCCESS_LOG,new Object[]{mode(),provided,this,null});
				return null;
			}
		
		DataProvider dp = provided.get(0).provider();
	
		if (!(dp instanceof ValueProvider))
			if (mode()==STRICT)
					throw new RuntimeException(format(INPUT_ERROR,mode(),this,provided));
			else {
				logger.trace(BINDING_SUCCESS_LOG,new Object[]{mode(),dp,this,null});
				return null;
			}
	
		ValueProvider vp = (ValueProvider) dp;
		
		Object input = vp.get();
		TYPE result = null;
		
		Throwable error=null;
		try{
			result = accept(input);
		}
		catch(Throwable t) {
			error=t;
		}
		
		if (result == null && mode()==STRICT)
			throw error==null?
					new RuntimeException(format(BINDING_ERROR,mode(),input,this)):
					new RuntimeException(format(BINDING_ERROR,mode(),input,this),error);
		
		logger.trace(BINDING_SUCCESS_LOG,new Object[]{mode(),dp,this,result});
		
		return result;
	};
	

	protected abstract TYPE accept(Object o);
	
	
	public BinderProvider<TYPE> provider() {
		logger.trace(BUILT_LOG,AbstractPrimitiveBinder.this,mode());
		return new BinderProvider<TYPE>() {
			
			@Override public Key<? extends TYPE> matchingKey() {
				return key();
			}
			
			@Override
			public TypeBinder<TYPE> binder(Key<TYPE> key, Environment factory) {
				return AbstractPrimitiveBinder.this; 
			}
		};
	}

}
