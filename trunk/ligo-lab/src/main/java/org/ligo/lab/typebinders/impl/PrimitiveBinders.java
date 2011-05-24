/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static org.ligo.lab.typebinders.Key.*;


/**
 * @author Fabio Simeoni
 *
 */
public class PrimitiveBinders {

	public static class StringBinder extends AbstractPrimitiveBinder<String> {
		
		StringBinder() {
			super(get(String.class));
		}
		
		/**{@inheritDoc}*/
		@Override
		protected String accept(Object o) {
			return o instanceof String? (String) o : null;
		}
	}
	
	public static class IntBinder extends AbstractPrimitiveBinder<Integer> {
		
		public IntBinder() {
			super(get(Integer.class));
		}
		
		/**{@inheritDoc}*/
		@Override
		protected Integer accept(Object o) {
			return o instanceof Integer? (Integer) o : 
				o instanceof String? Integer.valueOf((String)o):
					null;
		}
	}
	
	public static class LongBinder extends AbstractPrimitiveBinder<Long> {
		
		public LongBinder() {
			super(get(Long.class));
		}
		
		/**{@inheritDoc}*/
		@Override
		protected Long accept(Object o) {
			return o instanceof Long? (Long) o : 
				o instanceof String? Long.valueOf((String)o):
					null;
		}
	}
	
	public static class FloatBinder extends AbstractPrimitiveBinder<Float> {
		
		public FloatBinder() {
			super(get(Float.class));
		}
		
		/**{@inheritDoc}*/
		@Override
		protected Float accept(Object o) {
			return o instanceof Float? (Float) o : 
				o instanceof String? Long.valueOf((String)o):
					null;
		}
	}
	
	public static class DoubleBinder extends AbstractPrimitiveBinder<Double> {
		
		public DoubleBinder() {
			super(get(Double.class));
		}
		
		/**{@inheritDoc}*/
		@Override
		protected Double accept(Object o) {
			return o instanceof Double? (Double) o : 
				o instanceof String? Double.valueOf((String)o):
					null;
		}
	}

	public static class BooleanBinder extends AbstractPrimitiveBinder<Boolean> {
		
		public BooleanBinder() {
			super(get(Boolean.class));
		}
		
		/**{@inheritDoc}*/
		@Override
		protected Boolean accept(Object o) {
			return o instanceof Boolean? (Boolean) o : 
				o instanceof String? Boolean.valueOf((String)o):
					null;
		}
	}
	
}
