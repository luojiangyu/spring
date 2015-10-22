package org.core.beans.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.core.beans.BeanDefinition;
import org.core.beans.factory.support.BeanDefinitionRegistry;

public class DefaultListableBeanFactory extends AutowireCapableBeanFactory implements BeanDefinitionRegistry {
	public final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
	public final List<String> beanNames = new ArrayList<String>();
    public static final DefaultListableBeanFactory INSTANCE =new DefaultListableBeanFactory();
	public void registryBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception {
		BeanDefinition oldBeanDefinition = beanDefinitionMap.get(beanName);
		if(oldBeanDefinition!=null){
			throw new Exception();
		}
		beanDefinitionMap.put(beanName, beanDefinition);
		beanNames.add(beanName);
	}

	public BeanDefinition getBeanDefinition(String beanName) {
		// TODO Auto-generated method stub
		return beanDefinitionMap.get(beanName);
	}
}
