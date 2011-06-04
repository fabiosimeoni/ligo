package org.ligo.core.binders.impl;

import static java.util.Collections.*;
import static org.ligo.core.binders.api.BindMode.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.List;

import javax.xml.namespace.QName;

import org.ligo.core.annotations.AbstractBindAdapter;
import org.ligo.core.annotations.BindAdapter;
import org.ligo.core.binders.api.Environment;
import org.ligo.core.binders.api.TypeBinder;
import org.ligo.data.LigoData;
import org.ligo.data.LigoObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdaptedParameterBinder<M extends Member> extends AbstractParameterBinder<M> {

	private static Logger logger = LoggerFactory.getLogger(AdaptedParameterBinder.class);
	
	private final BindAdapter annotation;
	private final AbstractBindAdapter<?,?> adapter;
	
	public AdaptedParameterBinder(BoundParameter<M> param, Environment env) {

		super(env);
		
		annotation = (BindAdapter) param.bindingAnnotation();
	
		setBoundName(new QName(annotation.ns(),annotation.value()));
		
		@SuppressWarnings("unchecked")
		Class<? extends AbstractBindAdapter> adapterClass = annotation.adapter();
		
		adapter =  env.resolver().resolve(adapterClass, emptyList());

		@SuppressWarnings("unchecked")
		AdaptedBinder<?,?> bindingAdapter = (AdaptedBinder<?,?>) new AdaptedBinder(adapter);
		
		setBinder(bindingAdapter);	
		

	}
	
	public Object bind(LigoObject ligoObject) {
		
		//set mode, lazily on potentially cached binders
		if (annotation.mode()!=DEFAULT)
			binder().setMode(annotation.mode());
		
		List<? extends LigoData> data = environment().expressionResolver().resolve(boundName(),ligoObject);
		return binder().bind(data);
	
	}
	
	
	private class AdaptedBinder<INTYPE,OUTTYPE> extends AbstractBinder<OUTTYPE> {

		private final TypeBinder<INTYPE> inBinder;
		private final AbstractBindAdapter<INTYPE,OUTTYPE> adapter;

		protected AdaptedBinder(AbstractBindAdapter<INTYPE,OUTTYPE> a) {
			super(a.outKey());
			adapter = a;
			inBinder = environment().binderFor(adapter.inKey());
			
		}

		/**{@inheritDoc}*/
		@Override
		public OUTTYPE bind(List<? extends LigoData> i) {
			INTYPE bound = inBinder.bind(i);
			OUTTYPE adapted = adapter.bind(bound);
			logger.trace("bound {} to {}",bound,adapted);
			return adapted;
		}
		
		/**{@inheritDoc}*/
		@Override
		public String toString() {
			return inBinder.toString();
		}
	}
	
	static ParameterBinderProvider provider () {
		
		return new ParameterBinderProvider() {
			
			@Override
			public Class<? extends Annotation> matchingAnnotation() {
				return BindAdapter.class;
			}
			
			@Override
			public <M extends Member> ParameterBinder<M> binder(BoundParameter<M> context, Environment env) {
				return new AdaptedParameterBinder<M>(context,env);
			}
		};
		
	}
}