package org.core;

import org.core.context.ApplicationContext;
import org.core.context.ClassPathXmlApplicationContext;
import org.core.test.Zoo;
import org.junit.Test;



public class ApplicationTest {
	@Test
	public void testRead() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		 Zoo zoo = (Zoo) applicationContext.getBean("zoo");
	     zoo.showAnimals();
	}
}
