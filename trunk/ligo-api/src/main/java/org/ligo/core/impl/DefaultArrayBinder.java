/**
 * 
 */
package org.ligo.core.impl;

import static java.lang.String.*;
import static org.ligo.core.keys.Keys.*;
import static org.ligo.core.kinds.Kind.*;
import static org.ligo.core.kinds.Kind.KindValue.*;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.ligo.core.ArrayBinder;
import org.ligo.core.CollectionBinder;
import org.ligo.core.Environment;
import org.ligo.core.TypeBinder;
import org.ligo.core.data.Provided;
import org.ligo.core.keys.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Fabio Simeoni
 *
 */
class DefaultArrayBinder<TYPE> extends AbstractBinder<TYPE[]> implements ArrayBinder<TYPE> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultArrayBinder.class);
	
	static final String TO_STRING= "%1s-array(%2s)";
	
	private final CollectionBinder<List<TYPE>,TYPE> backing;
	
	@SuppressWarnings("unchecked")
	DefaultArrayBinder(final Key<? extends TYPE[]> key, Environment e) {
		
		super(key);
		
		ParameterizedType type = new ParameterizedType() {
			@Override public Type getRawType() {return List.class;}
			@Override public Type getOwnerType() {return null;}
			@Override public Type[] getActualTypeArguments() {
				return new Type[]{
						key.kind().value()==CLASS?
							key.toClass().getComponentType(): 
							GENERICARRAY(key.kind()).getGenericComponentType()
				};
			}
		};
		
		TypeBinder<List<TYPE>> binder = (CollectionBinder) (TypeBinder) e.binderFor(newKey(type,key.qualifier()));
		
		setMode(binder.mode());
		
		if (!(binder instanceof CollectionBinder<?,?>))
			throw new RuntimeException("Binding iterators requires binding collections, " +
							"but no collection binder was registered");
		
		backing= (CollectionBinder) binder;
		
	}
	
	@Override
	public TypeBinder<?> binder() {
		return backing.binder();
	}
	
	@Override
	public TYPE[] bind(List<Provided> provided) {
		
		List<TYPE> list = backing.bind(provided);
		
		Class<?> componentClass = key().toClass().getComponentType();
		
		@SuppressWarnings("unchecked")
		TYPE[] array = (TYPE[]) Array.newInstance(componentClass,list.size());
		
		array = list.toArray(array);
			
		logger.trace(BINDING_SUCCESS_LOG,new Object[]{this,provided,array});
		
		return array;
	}

	/**{@inheritDoc}*/
	@Override
	public String toString() {
		return format(TO_STRING,super.toString(),binder());
	}
	
	public static BinderProvider<Object[]> provider() {
		
		return new BinderProvider<Object[]>() {

			@Override
			public TypeBinder<Object[]> binder(Key<Object[]> key, Environment env) {
				return new DefaultArrayBinder<Object>(key,env);
			}			
			
			@SuppressWarnings("unchecked")
			@Override public Key<Object[]> matchingKey() {
				return (Key) newKey(Object[].class);
			}
		};
	}
	
}
