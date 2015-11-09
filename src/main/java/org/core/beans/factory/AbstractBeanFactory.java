package org.core.beans.factory;

import org.core.beans.AnnotationBeanDefinition;
import org.core.beans.BeanDefinition;
import org.core.beans.SimpleBeanDefinition;

public abstract class AbstractBeanFactory implements BeanFactory {
	public Object getBean(String name) {
		Object obj = null;
		if (containsBean(name)) {
			return getBeanDefinition(name).getBeanClass();
		} else {
			try {
				obj = createBean(name);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return obj;
	}

	public boolean containsBean(String name) {
		BeanDefinition beanDefinition = getBeanDefinition(name);
		beanDefinition = beanDefinition instanceof AnnotationBeanDefinition ? (AnnotationBeanDefinition) beanDefinition
				: (SimpleBeanDefinition) beanDefinition;
		if (beanDefinition.getBeanClass() != null) {
			return true;
		}
		return false;

	}

	public abstract Object createBean(String name)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException;

	public abstract BeanDefinition getBeanDefinition(String name);
}
