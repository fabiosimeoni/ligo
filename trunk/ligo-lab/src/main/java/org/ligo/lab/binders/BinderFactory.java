/**
 * 
 */
package org.ligo.lab.binders;

import org.ligo.lab.typebinders.Key;

/**
 * @author Fabio Simeoni
 *
 */
public interface BinderFactory<TYPE,IN,OUT> extends Binder<Key<TYPE>, Binder<IN,OUT>> {}
