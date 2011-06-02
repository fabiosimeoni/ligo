package org.ligo.core.binders.impl;

import static org.ligo.core.binders.BindMode.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.core.annotations.Bind;
import org.ligo.core.binders.Environment;
import org.ligo.data.LigoData;
import org.ligo.data.LigoObject;

public class DefaultParameterBinder<M extends Member> extends AbstractParameterBinder<M> {
	
	private final Bind annotation;

	
	public DefaultParameterBinder(BoundParameter<M> param, Environment env) {

		super(env);
		
		annotation = (Bind) param.bindingAnnotation();
		setBoundName(new QName(annotation.ns(),annotation.value()));
		setBinder(env.binderFor(param.key()));
		

	}
	
	@Override
	public Object bind(LigoObject ligoObject) {
		
		//set mode, lazily on potentially cached binders
		if (annotation.mode()!=DEFAULT)
			binder().setMode(annotation.mode());
		
		List<LigoData> data = environment().expressionResolver().resolve(boundName(),ligoObject);
		return binder().bind(data);
	
	}
	
	static ParameterBinderProvider provider () {
		
		return new ParameterBinderProvider() {
			
			@Override
			public Class<? extends Annotation> matchingAnnotation() {
				return Bind.class;
			}
			
			@Override
			public <M extends Member> ParameterBinder<M> binder(BoundParameter<M> param, Environment env) {
				return new DefaultParameterBinder<M>(param,env);
			}
		};
		
	}
}