/**
 * 
 */
package org.ligo.lab.typebinders.impl;

import static org.ligo.lab.typebinders.Bind.Mode.*;
import static org.ligo.lab.typebinders.Key.*;


/**
 * @author Fabio Simeoni
 *
 */
public class PrimitiveBinders {

	public static class StringBinder extends AbstractPrimitiveBinder<String> {
		
		public StringBinder() {
			super(get(String.class));
		}
		
		/**{@inheritDoc}*/
		@Override
		protected String accept(Object o) {
			return o instanceof String? (String) o : 
								mode()==STRICT? null: o.toString();
		}
	}
	
	public static class IntBinder extends AbstractPrimitiveBinder<Integer> {
		
		public IntBinder() {
			super(get(Integer.class));
		}
		
		/**{@inheritDoc}*/
		@Override
		protected Integer accept(Object o) {
			try {
				return o instanceof Integer? (Integer) o :Integer.valueOf((String)o);
			} 
			catch (Exception e) {
				if (mode()==STRICT)
					throw new RuntimeException(e);
				else
					return 0;
			}
				
		}
	}
	
	public static class LongBinder extends AbstractPrimitiveBinder<Long> {
		
		public LongBinder() {
			super(get(Long.class));
		}
		
		/**{@inheritDoc}*/
		@Override
		protected Long accept(Object o) {
			try {
				return o instanceof Long? (Long) o : Long.valueOf((String)o);
			} 
			catch (Exception e) {
				if (mode()==STRICT)
					throw new RuntimeException(e);
				else
					return 0L;
			}
			
		}
	}
	
	public static class FloatBinder extends AbstractPrimitiveBinder<Float> {
		
		public FloatBinder() {
			super(get(Float.class));
		}
		
		/**{@inheritDoc}*/
		@Override
		protected Float accept(Object o) {
			try {
				return o instanceof Float? (Float) o : Float.valueOf((String)o);
			} 
			catch (Exception e) {
				if (mode()==STRICT)
					throw new RuntimeException(e);
				else
					return 0f;
			}
		}
	}
	
	public static class DoubleBinder extends AbstractPrimitiveBinder<Double> {
		
		public DoubleBinder() {
			super(get(Double.class));
		}
		
		/**{@inheritDoc}*/
		@Override
		protected Double accept(Object o) {
			try {
				return o instanceof Double? (Double) o : Double.valueOf((String)o);
			} 
			catch (Exception e) {
				if (mode()==STRICT)
					throw new RuntimeException(e);
				else
					return 0d;
			}
		}
	}

	public static class BooleanBinder extends AbstractPrimitiveBinder<Boolean> {
		
		public BooleanBinder() {
			super(get(Boolean.class));
		}
		
		/**{@inheritDoc}*/
		@Override
		protected Boolean accept(Object o) {
				try {
					return o instanceof Boolean? (Boolean) o : Boolean.valueOf((String)o);
				} 
				catch (Exception e) {
					if (mode()==STRICT)
						throw new RuntimeException(e);
					else
						return false;
				}
		}
	}
	
}
