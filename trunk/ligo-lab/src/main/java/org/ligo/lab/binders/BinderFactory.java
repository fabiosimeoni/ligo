/**
 * 
 */
package org.ligo.lab.binders;

/**
 * @author Fabio Simeoni
 *
 */
public interface BinderFactory<SEED,IN,OUT> extends Binder<SEED, Binder<IN,OUT>> {}
