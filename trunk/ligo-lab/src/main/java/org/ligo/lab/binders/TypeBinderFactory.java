/**
 * 
 */
package org.ligo.lab.binders;

/**
 * @author Fabio Simeoni
 *
 */
public interface TypeBinderFactory<IN,TYPE> extends Binder<Class<TYPE>, Binder<IN,TYPE>> {}
