/**
 * 
 */
package org.ligo.app;

import org.ligo.api.Project;

/**
 * @author Fabio Simeoni
 *
 */
public class Managed {

	private String s;
	
	
	/**
	 * @param s the s to set
	 */
	public void setS(@Project("test") String s) {
		this.s = s;
	}
	
	/**
	 * @return the s
	 */
	public String getS() {
		return s;
	}
}
