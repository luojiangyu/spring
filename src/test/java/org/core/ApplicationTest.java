package org.core;


import org.core.context.ApplicationContext;
import org.core.context.ClassPathXmlApplicationContext;
import org.core.test.LoginController;
import org.core.test.Zoo;
import org.junit.Test;



public class ApplicationTest {
	@Test
	public void testRead() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		 LoginController login = (LoginController) applicationContext.getBean("login");
		 login.login();
	}
	
}
