package org.core.beans.factory.support;

import org.core.beans.BeanDefinition;

public interface BeanDefinitionRegistry {
	public void registryBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception;

	public BeanDefinition getBeanDefinition(String beanName);
}
