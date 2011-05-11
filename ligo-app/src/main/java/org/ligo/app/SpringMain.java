/**
 * 
 */
package org.ligo.app;

import org.ligo.nodes.LigoConfiguration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Fabio Simeoni
 *
 */
public class SpringMain {

	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/beans.xml");
		
		LigoConfiguration config = context.getBean(LigoConfiguration.class);
		System.out.println(config);
	}
}
