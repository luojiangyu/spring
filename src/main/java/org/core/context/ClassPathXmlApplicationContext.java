package org.core.context;

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {
	public ClassPathXmlApplicationContext(String configLocation) {
		this(new String[] { configLocation });
	}

	public ClassPathXmlApplicationContext(String[] configLocations) {
		super();
		try {
			setConfigLocations(configLocations);
			refresh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
