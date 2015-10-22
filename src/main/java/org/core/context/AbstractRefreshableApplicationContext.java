package org.core.context;

import org.core.beans.factory.DefaultListableBeanFactory;

public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {
	private DefaultListableBeanFactory beanFactory;
	private String[] configLocations;

	protected final void refreshBeanFactory() {
		DefaultListableBeanFactory beanFactory = createBeanFactory();
		loadBeanDefinitions(beanFactory);
		this.beanFactory=beanFactory;
	}

	public DefaultListableBeanFactory obtainBeanFactory() {
		refreshBeanFactory();
		return this.beanFactory;
	}

	protected DefaultListableBeanFactory createBeanFactory() {
		return DefaultListableBeanFactory.INSTANCE;
	}

	public DefaultListableBeanFactory getBeanFactory() {
		return this.beanFactory;
	}

	public String[] getConfigLocations() {
		return this.configLocations;
	}

	public void setConfigLocations(String[] configLocations) throws Exception {
		if (configLocations == null) {
			throw new Exception();
		}
		if (configLocations != null) {
			String[] location = new String[configLocations.length];
			int i = 0;
			for (String configLocation : configLocations) {
				location[i++] = configLocation.trim();
			}
			this.configLocations = location;
		}

	}

	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {

	}
}
