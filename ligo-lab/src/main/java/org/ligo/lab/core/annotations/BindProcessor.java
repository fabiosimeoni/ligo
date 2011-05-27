/**
 * 
 */
package org.ligo.lab.core.annotations;

import static java.lang.String.*;
import static org.ligo.lab.core.annotations.Bind.Mode.*;

import java.lang.reflect.Constructor;

import javax.xml.namespace.QName;

import org.ligo.lab.core.Environment;
import org.ligo.lab.core.TypeBinder;
import org.ligo.lab.core.impl.BindingAdapter;
import org.ligo.lab.core.impl.ParameterContext;
import org.ligo.lab.core.impl.AbstractMethodDef.NamedBinder;


/**
 * @author Fabio Simeoni
 *
 */
public class BindProcessor implements AnnotationProcessor {
	
	public NamedBinder binderFor(ParameterContext context, Environment env) {
		
		Bind bindAnnotation = (Bind) context.bindingAnnotation();
		
		QName name = new QName(bindAnnotation.ns(),bindAnnotation.value());
		
		TypeBinder<?> binder;
		if (!bindAnnotation.adapter().equals(BindingAdapter.class)) {
			
			try {
				
				@SuppressWarnings("unchecked")
				Class<? extends BindingAdapter> adapterClass = bindAnnotation.adapter();
				
				Constructor<?> c = adapterClass.getDeclaredConstructor();
				c.setAccessible(true);
				
				BindingAdapter<?,?> adapter = (BindingAdapter<?,?>) c.newInstance();
				
				@SuppressWarnings("unchecked")
				AdaptedBinder<?,?> bindingAdapter = (AdaptedBinder<?,?>) new AdaptedBinder(adapter,env);
				
				binder = bindingAdapter;	
				
			}
			catch(Exception e) {
				throw new RuntimeException(format("could not instantiate adapter %1s",bindAnnotation.adapter()),e);
			}
		}
		else 
		//recur to obtain binder for type
			binder = env.binderFor(context.key());
		
		//set mode
		if (bindAnnotation.mode()!=DEFAULT)
			binder.setMode(bindAnnotation.mode());	
		
		return new NamedBinder(name,binder);
		
	
	};
}
