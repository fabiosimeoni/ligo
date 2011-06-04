/**
 * 
 */
package org.ligo.core.binders.impl;

import static java.lang.String.*;
import static org.ligo.core.binders.api.BindMode.*;
import static org.ligo.core.binders.impl.MethodBinder.*;
import static org.ligo.core.keys.Keys.*;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.ligo.core.binders.api.Environment;
import org.ligo.core.binders.api.ObjectBinder;
import org.ligo.core.binders.api.TypeBinder;
import org.ligo.core.keys.Key;
import org.ligo.data.LigoData;
import org.ligo.data.LigoObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link TypeBinder} for arbitrary object structures.
 * 
 * @author Fabio Simeoni
 *
 */
class DefaultObjectBinder<T> extends AbstractBinder<T> implements ObjectBinder<T> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultObjectBinder.class);
	
	static final String TO_STRING= "%1s-obj%2s";
	static final String DUPLICATE_NAME= "ambiguous binding: name '%1s' is bound in %2s but also elsewhere in %3s";
	static String CARDINALITY_ERROR = "%1s required one object but found: %2s";
	
	private final Environment env;
	
	private final ConstructorBinder cBinder;
	private final List<MethodBinder> mBinders;
	
	private Map<QName,TypeBinder<?>> binders = new HashMap<QName, TypeBinder<?>>();

	
	public DefaultObjectBinder(Key<? extends T> key, Environment e) {
		
		super(key);
		env = e;
		
		//analyse class
		cBinder = new ConstructorBinder(key,env);
		for (ParameterBinder<?> pbinder : cBinder.parameterBinders())
			addBinder(cBinder.member(),pbinder);
		
		mBinders = getMethodBinders(key,env);
		for (MethodBinder mBinder : mBinders)
			for (ParameterBinder<?> pbinder : mBinder.parameterBinders())
				addBinder(mBinder.member(),pbinder);
	}

	//stores and guards uniqueness of bound names
	void addBinder(Member m, ParameterBinder<?> pbinder) {
		
		//some loss of modularity here
		if (pbinder instanceof ConstantParameterBinder<?>)
			return;
		
		if (binders.containsKey(pbinder.boundName()))
			throw new RuntimeException(format(DUPLICATE_NAME,pbinder.boundName(),m,key()));
		else
		 binders.put(pbinder.boundName(), pbinder.binder());
	}
	
	
	/**{@inheritDoc}*/
	public Map<QName,TypeBinder<?>> binders() {
		return new HashMap<QName, TypeBinder<?>>(binders);
	}
	
	/**{@inheritDoc}*/
	@Override
	public T bind(List<? extends LigoData> data) {
		
		try {
			
			if (data.size()!=1 || !(data.get(0) instanceof LigoObject)) {
				if (mode()==STRICT)
					throw new RuntimeException(format(CARDINALITY_ERROR,this,data));
				else {
					logger.trace(BINDING_SUCCESS_LOG,new Object[]{data,this,null});
					return null;
				}
			}
			
			LigoObject ligoObject = (LigoObject) data.get(0);
			
			@SuppressWarnings("unchecked") //internally consistent
			T javaObject = (T) cBinder.bind(ligoObject);
			
			//pull method parameters and invoke
			for (MethodBinder mBinder : mBinders)
				mBinder.bind(javaObject,ligoObject);			
			
			logger.trace(BINDING_SUCCESS_LOG,new Object[]{ligoObject,this,javaObject});
			
			return javaObject;
		}
		catch(Throwable e) {
			throw new RuntimeException(format(BINDING_ERROR,key(),data),e);
		}
	}
	
	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(TO_STRING,super.toString(),binders);
	}
	
	
	public static BinderProvider<Object> provider() {
		
		return new BinderProvider<Object>() {
			
			@Override
			public Key<Object> matchingKey() {
				return newKey(Object.class);
			}
	
			@Override
			public TypeBinder<Object> binder(Key<Object> key, Environment env) {
				return new DefaultObjectBinder<Object>(key,env);
			}
		};
	}
}
