/**
 * 
 */
package org.ligo.lab.binders;

/**
 * @author Fabio Simeoni
 *
 */
public interface BinderFactory<IN,TYPE> extends Binder<Class<TYPE>, TypeBinder<IN,TYPE>> {}
