/**
 * 
 */
package org.ligo.lab.core.annotations;

import static java.lang.String.*;
import static java.util.Collections.*;

import javax.xml.namespace.QName;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.impl.BindingAdapter;
import org.ligo.lab.core.impl.ParameterBinder;
import org.ligo.lab.core.impl.ParameterContext;


/**
 * @author Fabio Simeoni
 *
 */
public class BindProcessor implements AnnotationProcessor {
	
	private static final String INSTANTIATION_ERROR = "could not instantiate adapter %1s";

	public ParameterBinder binderFor(ParameterContext context, Environment env) {
		
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
		
		return new ParameterBinder(name,binder,context);
			
	};
}
