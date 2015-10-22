package org.core.beans.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.core.beans.BeanDefinition;
import org.core.beans.BeanReference;
import org.core.beans.PropertyValue;

public abstract class AutowireCapableBeanFactory extends AbstractBeanFactory {
	public static final String OBJECT = "object";
	public static final String LIST = "list";
	public static final String VALUE = "value";
	public static final String REF = "ref";
	public static final String SET = "set";

	public Object createBean(String name)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		BeanDefinition beanDefinition = getBeanDefinition(name);
		String className = beanDefinition.getClassName();
		Class clazz = Class.forName(className);
		Object obj = clazz.newInstance();
		beanDefinition.setStatus("1");
		populateBean(obj, beanDefinition);
		beanDefinition.setBeanClass(obj);
		return obj;

	}

	public void populateBean(Object obj, BeanDefinition beanDefinition) {
		for (PropertyValue pv : beanDefinition.getPvs().getPropertyValues()) {
			if (pv.getValue() instanceof BeanReference) {
				BeanReference beanReference = (BeanReference) pv.getValue();

				if (OBJECT.equals(beanReference.getType())) {
					String ref = beanReference.getMappingBean()[0];
					if (getBeanDefinition(ref).getStatus().equals("1")) {
						continue;
					}
					this.invoke(obj, pv.getName(), getBean(ref));
				} else if (LIST.equals(beanReference.getType())) {
					this.invokeList(obj, pv.getName(), beanReference);
				}else if(SET.equals(beanReference.getType())){
				    this.invokeSet(obj, pv.getName(), beanReference);	
				}

			} else {
				this.invoke(obj, pv.getName(), pv.getValue());
			}
		}

	}

	protected void invoke(Object obj, String fieldName, Object value) {
		String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		Method method;
		try {
			method = obj.getClass().getMethod(methodName, obj.getClass().getDeclaredField(fieldName).getType());
			method.invoke(obj, value);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void invokeList(Object obj, String fieldName, BeanReference beanReference) {
		List<Object> list = new ArrayList<Object>();
		invokeCollection(obj, list, fieldName, beanReference);
	}

	public void invokeSet(Object obj, String fieldName, BeanReference beanReference) {
		Set<Object> set = new HashSet<Object>();
		invokeCollection(obj, set, fieldName, beanReference);
	}

	public void invokeCollection(Object obj, Collection<Object> collection, String fieldName,
			BeanReference beanReference) {
		String[] mappingBean = beanReference.getMappingBean();
		String[] mappingType = beanReference.getMappingType();
		for (int i = 0; i < mappingBean.length; i++) {
			if (VALUE.equals(mappingType[i])) {
				collection.add(mappingBean[i]);
			} else if (REF.equals(mappingType[i])) {
				collection.add(getBean(mappingBean[i]));
			}
		}
		this.invoke(obj, fieldName, collection);

	}

	public abstract BeanDefinition getBeanDefinition(String name);
}
