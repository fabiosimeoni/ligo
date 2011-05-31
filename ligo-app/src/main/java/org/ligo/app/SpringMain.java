/**
 * 
 */
package org.ligo.app;

import org.ligo.core.impl.LigoEnvironment;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Fabio Simeoni
 *
 */
public class SpringMain {

	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		
		LigoEnvironment config = context.getBean(LigoEnvironment.class);
		System.out.println(config);
	}
}
