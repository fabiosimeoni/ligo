/**
 * 
 */
package org.ligo.core.annotations;

import static java.lang.String.*;
import static java.util.Collections.*;

import java.lang.reflect.Member;

import javax.xml.namespace.QName;

import org.ligo.core.Environment;
import org.ligo.core.TypeBinder;
import org.ligo.core.impl.BindingAdapter;
import org.ligo.core.impl.ParameterBinder;
import org.ligo.core.impl.ParameterContext;


/**
 * @author Fabio Simeoni
 *
 */
public class BindProcessor implements AnnotationProcessor {
	
	private static final String INSTANTIATION_ERROR = "could not instantiate adapter %1s";

	public <M extends Member> ParameterBinder<M> binderFor(ParameterContext<M> context, Environment env) {
		
		Bind bindAnnotation = (Bind) context.bindingAnnotation();
		
		QName name = new QName(bindAnnotation.ns(),bindAnnotation.value());
		
		TypeBinder<?> binder;
		if (!bindAnnotation.adapter().equals(BindingAdapter.class)) {
			
			try {
				
				@SuppressWarnings("unchecked")
				Class<? extends BindingAdapter> adapterClass = bindAnnotation.adapter();
				
				BindingAdapter<?,?> adapter =  env.resolver().resolve(adapterClass, emptyList());

				@SuppressWarnings("unchecked")
				AdaptedBinder<?,?> bindingAdapter = (AdaptedBinder<?,?>) new AdaptedBinder(adapter,env);
				
				binder = bindingAdapter;	
				
			}
			catch(Exception e) {
				throw new RuntimeException(format(INSTANTIATION_ERROR,bindAnnotation.adapter()),e);
			}
		}
		else //recur to obtain binder for type
			binder = env.binderFor(context.key());
		
		return new ParameterBinder<M>(name,binder,context);
			
	};
}
