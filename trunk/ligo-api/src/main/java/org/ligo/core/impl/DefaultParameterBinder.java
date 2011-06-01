package org.ligo.core.impl;

import static org.ligo.core.BindMode.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;

import javax.xml.namespace.QName;

import org.ligo.core.Environment;
import org.ligo.core.annotations.Bind;
import org.ligo.core.data.LigoObject;

public class DefaultParameterBinder<M extends Member> extends AbstractParameterBinder<M> {
	
	private final Bind annotation;

	
	public DefaultParameterBinder(BoundParameter<M> param, Environment env) {

		super(env);
		
		annotation = (Bind) param.bindingAnnotation();
		setBoundName(new QName(annotation.ns(),annotation.value()));
		setBinder(env.binderFor(param.key()));
		

	}
	
	public Object bind(LigoObject sp) {
		
		//set mode, lazily on potentially cached binders
		if (annotation.mode()!=DEFAULT)
			binder().setMode(annotation.mode());
			
		return binder().bind(sp.get(boundName()));
	
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