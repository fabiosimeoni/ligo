/**
 * 
 */
package org.ligo.lab.typebinders;



/**
 * @author Fabio Simeoni
 *
 */
public interface ArrayBinder<TYPE> extends TypeBinder<TYPE[]>{

	TypeBinder<?> part();
}
