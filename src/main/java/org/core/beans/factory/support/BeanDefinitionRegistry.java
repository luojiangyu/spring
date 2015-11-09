package org.core.beans.factory.support;

import java.util.List;

import org.core.beans.BeanDefinition;


public interface BeanDefinitionRegistry {
	public void registryBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception;

	public BeanDefinition getBeanDefinition(String beanName);
	@SuppressWarnings("rawtypes")
	public void setExtendsMap(Class clazz);
	public List<String> getSubClass(String name);
}
