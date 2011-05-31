/**
 * 
 */
package org.ligo.app;

import org.ligo.core.annotations.Bind;

/**
 * @author Fabio Simeoni
 *
 */
public class Managed {

	private String s;
	
	
	/**
	 * @param s the s to set
	 */
	public void setS(@Bind("test") String s) {
		this.s = s;
	}
	
	/**
	 * @return the s
	 */
	public String getS() {
		return s;
	}
}
