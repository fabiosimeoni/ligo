package org.ligo.core.impl;

import static org.ligo.core.BindMode.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.core.Environment;
import org.ligo.core.annotations.Bind;
import org.ligo.core.data.LigoProvider;

public class DefaultParameterBinder<M extends Member> extends AbstractParameterBinder<M> {
	
	private final Bind annotation;

	
	public DefaultParameterBinder(BoundParameter<M> param, Environment env) {

		super(env);
		
		annotation = (Bind) param.bindingAnnotation();
		setBoundName(new QName(annotation.ns(),annotation.value()));
		setBinder(env.binderFor(param.key()));
		

	}
	
	@Override
	public Object bind(LigoProvider provider) {
		
		//set mode, lazily on potentially cached binders
		if (annotation.mode()!=DEFAULT)
			binder().setMode(annotation.mode());
		
		List<LigoProvider> providers = environment().expressionResolver().resolve(boundName(),provider);
		return binder().bind(providers);
	
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