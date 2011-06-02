/**
 * 
 */
package org.ligo.core.binders.impl;

import static java.lang.String.*;
import static org.ligo.core.utils.ReflectionUtils.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.core.annotations.BindConstant;
import org.ligo.core.binders.api.Environment;
import org.ligo.data.LigoData;
import org.ligo.data.LigoObject;

/**
 * @author Fabio Simeoni
 *
 */
public class ConstantParameterBinder<M extends Member> extends AbstractParameterBinder<M>  {

	private static final QName UNBOUND_PARAM=new QName("_const_");
	
	private final Object constant;
	private final Class<?> clazz;

	/**{@inheritDoc}*/
	public ConstantParameterBinder(BoundParameter<M> param, Environment env) {
		
		super(env);
		
		clazz = param.key().toClass();
		
		constant = defaultOf(clazz);
		
		if (clazz==null)
			throw new RuntimeException(format("cannot bind constant to %1s",clazz));
		
		
		setBoundName(UNBOUND_PARAM);
		
		setBinder(new AbstractBinder<Object>(param.key()) {

			@Override public Object bind(List<LigoData> data) {
				return constant;
			}
			
			@Override public String toString() {
				return constant.toString();
			}
		
		});
				
	}
	
	/**{@inheritDoc}*/
	@Override
	public Object bind(LigoObject ligoObject) {
		return binder().bind((LigoObject) null); //can pass anything, will be ignored
	}
	
	static ParameterBinderProvider provider () {
		
		return new ParameterBinderProvider() {
			
			@Override
			public Class<? extends Annotation> matchingAnnotation() {
				return BindConstant.class;
			}
			
			@Override
			public <M extends Member> ParameterBinder<M> binder(BoundParameter<M> context, Environment env) {
				return new ConstantParameterBinder<M>(context,env);
			}
		};
		
	}
	
//	public ConstantParameterBinder(Object co, Type t) {
//		
//		setClass(t);
//		
//		if ((co==null && clazz.isPrimitive()) ||
//			(co!=null && !clazz.isAssignableFrom(co.getClass())))
//				throw new RuntimeException(format("cannot set constant %1s on %2s",co,clazz));
//			
//		constant=co;
//	}
}
