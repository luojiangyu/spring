package org.core.beans;

import org.core.beans.factory.support.BeanDefinitionRegistry;
import org.core.beans.io.DefaultResourceLoader;
import org.core.beans.io.Resource;
import org.core.beans.io.ResourceLoader;


public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {
	private ResourceLoader resourceLoader;
	private BeanDefinitionRegistry beanDefinitionRegistry;

	public AbstractBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
		this.beanDefinitionRegistry = beanDefinitionRegistry;
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public BeanDefinitionRegistry getBeanDefinitionRegistry() {
		return beanDefinitionRegistry;
	}

	public void setBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) {
		this.beanDefinitionRegistry = beanDefinitionRegistry;
	}

	public int loadBeanDefinitions(String... configLocations) {
		int result =0;
		for(String location:configLocations){
			result+=loadBeanDefinitions(location);
		}
        return result;
	}
	public int loadBeanDefinitions(String configLocation){
		ResourceLoader resourceLoader =getResourceLoader();
		int result=0;
		if(resourceLoader instanceof DefaultResourceLoader){
			Resource resource=resourceLoader.getResource(configLocation);
			result=loadBeanDefinitions(resource);
		}
		return result;
	}


	

}
