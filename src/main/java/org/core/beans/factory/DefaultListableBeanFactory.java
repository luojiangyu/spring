package org.core.beans.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.core.beans.BeanDefinition;
import org.core.beans.factory.support.BeanDefinitionRegistry;

public class DefaultListableBeanFactory extends AutowireCapableBeanFactory implements BeanDefinitionRegistry {
	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
	public final List<String> beanNames = new ArrayList<String>();
	private Map<String, List<String>> extendsMap = new ConcurrentHashMap<String, List<String>>();
	public static final DefaultListableBeanFactory INSTANCE = new DefaultListableBeanFactory();

	public void registryBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception {
		BeanDefinition oldBeanDefinition = beanDefinitionMap.get(beanName);
		if (oldBeanDefinition != null) {
			throw new Exception();
		}
		beanDefinitionMap.put(beanName, beanDefinition);
		beanNames.add(beanName);
	}

	@SuppressWarnings("rawtypes")
	public void setExtendsMap(Class clazz) {
		Class parent = clazz.getSuperclass();
		while (parent != null) {
			if (extendsMap.containsKey(parent.getName())) {
				List<String> temp = extendsMap.get(parent.getName());
				temp.add(clazz.getName());
				extendsMap.put(parent.getName(), temp);
			} else {
				List<String> input = new ArrayList<String>();
				input.add(clazz.getName());
				extendsMap.put(parent.getName(), input);
				
			}
			parent = parent.getSuperclass();
		};
			Class[] interfaces = clazz.getInterfaces();
			for (Class interfaceItem : interfaces) {
				if(extendsMap.containsKey(interfaceItem.getName())){
					List<String> temp= extendsMap.get(interfaceItem.getName());
					temp.add(clazz.getName());
					extendsMap.put(interfaceItem.getName(), temp);
				}else{
					List<String> input=new ArrayList<String>();
					input.add(clazz.getName());
					extendsMap.put(interfaceItem.getName(), input);
				}
			}

	}

	public List<String> getSubClass(String name) {
		return extendsMap.get(name);
	}

	public BeanDefinition getBeanDefinition(String beanName) {
		// TODO Auto-generated method stub
		return beanDefinitionMap.get(beanName);
	}
}
