/**
 * 
 */
package org.ligo.core.impl;

import static java.lang.String.*;
import static org.ligo.core.kinds.Kind.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ligo.core.Environment;
import org.ligo.core.annotations.Bind;
import org.ligo.core.annotations.BindAdapter;
import org.ligo.core.annotations.BindConstant;
import org.ligo.core.keys.Key;


/**
 * @author Fabio Simeoni
 *
 */
public abstract class MemberBinder<M extends Member> {

	private static final String UNKNOWN_ANNOTATION_ERROR = "unknown annotation %1s";

	static final String INVALID_BIND_ONMETHOD_ERROR= 
		"Binding annotations are allowed only on single-type constructors/methods, this is not the case for %1s, annotate individual parameters instead";
	
	static final String PARTIAL_BINDING_ERROR= "%1s is only partially bound, some parameter have no binding annotations";
	
	private static Map<Class<? extends Annotation>,ParameterBinderProvider> processors =
		new HashMap<Class<? extends Annotation>, ParameterBinderProvider>();

	
	static {
		processors.put(Bind.class, DefaultParameterBinder.provider());
		processors.put(BindConstant.class, ConstantParameterBinder.provider());
		processors.put(BindAdapter.class, AdaptedParameterBinder.provider());
	}
	
	private List<ParameterBinder<M>> binders;
	private final Environment env;
	private final Key<?> key;
	private M member;

	public MemberBinder(Key<?> k, Environment e) {
		key=k;
		env=e;
	}
	
	/**
	 * @return the member
	 */
	public M member() {
		return member;
	}
	
	void setMember(M member) {
		this.member = member;
	}
	
	public List<ParameterBinder<M>> parameterBinders() {
		return binders;
	}
	
	Environment environment() {
		return env;
	}
	
	Key<?> key() {
		return key;
	}
	
	void setParameterBinders(List<BoundParameter<M>> parameters) {
		
		binders = new ArrayList<ParameterBinder<M>>();
		
		for (BoundParameter<M> param : parameters) {
			ParameterBinderProvider provider = processors.get(param.bindingAnnotation().annotationType()); 
			if (provider==null)
				throw new RuntimeException(format(UNKNOWN_ANNOTATION_ERROR,param.bindingAnnotation()));
			binders.add(provider.binder(param, env));
		}

	}
	
	List<BoundParameter<M>> getParameters(M m, Type[] types, Annotation[][] as) {
		
		List<BoundParameter<M>> parameters = new ArrayList<BoundParameter<M>>();
		boolean unbound=false;
		for (int i=0; i<types.length;i++) {
			BoundParameter<M> param = new BoundParameter<M>(m, kindOf(types[i]), as[i]); 
			if (param.isBound())
				parameters.add(param);
			else 
				unbound=true;
		}
		if (parameters.size()>0 && unbound)
			throw new RuntimeException(format(PARTIAL_BINDING_ERROR,m));
		
		return parameters;
	}
}
