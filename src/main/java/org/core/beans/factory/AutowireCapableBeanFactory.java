package org.core.beans.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.core.beans.SimpleBeanDefinition;
import org.core.beans.constant.AnnotationMetaType;
import org.core.beans.constant.AnnotationType;
import org.core.beans.constant.BeanStatus;
import org.core.beans.factory.annotation.Autowired;
import org.core.beans.factory.annotation.Component;
import org.core.beans.factory.annotation.Value;
import org.core.beans.AnnotationBeanDefinition;
import org.core.beans.AnnotationMeta;
import org.core.beans.BeanDefinition;
import org.core.beans.BeanReference;
import org.core.beans.PropertyValue;
import org.core.beans.PropertyValues;

public abstract class AutowireCapableBeanFactory extends AbstractBeanFactory {
	public static final String OBJECT = "object";
	public static final String LIST = "list";
	public static final String VALUE = "value";
	public static final String REF = "ref";
	public static final String SET = "set";

	@SuppressWarnings("rawtypes")
	public Object createBean(String name)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		BeanDefinition beanDefinition = getBeanDefinition(name);
		String className = beanDefinition.getClassName();
		Class clazz = Class.forName(className);
		Object obj = getBeanInstance(clazz, beanDefinition);
		if (obj == null) {

			obj = clazz.newInstance();
		}

		beanDefinition.setStatus(BeanStatus.INIT);
		populateBean(obj, beanDefinition);
		beanDefinition.setBeanClass(obj);
		beanDefinition.setStatus(BeanStatus.AVAILABLE);
		return obj;

	}

	@SuppressWarnings("rawtypes")
	public Object getBeanInstance(Class clazz, BeanDefinition beanDefinition) {
		Object result = null;
		if (beanDefinition instanceof AnnotationBeanDefinition) {
			result = getAnnotationBeanInstance(((AnnotationBeanDefinition) beanDefinition).getConstructorMeta());
		} else if (beanDefinition instanceof SimpleBeanDefinition) {
			try {
				result = getSimpleBeanInstance(clazz, ((SimpleBeanDefinition) beanDefinition).getConstructorArgs());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getSimpleBeanInstance(Class clazz, PropertyValues pvs) throws ClassNotFoundException {
		if (pvs == null || pvs.getPropertyValues().isEmpty()) {
			return null;
		}
		Object result = null;
		List<PropertyValue> pvList = pvs.getPropertyValues();
		Object[] parameter = new Object[pvList.size()];
		Class[] parameterType = new Class[pvList.size()];
		int len = 0;
		for (PropertyValue pv : pvList) {
			if (pv.getValue() instanceof BeanReference) {
				BeanReference beanReference = (BeanReference) pv.getValue();
				if (OBJECT.equals(beanReference.getType())) {
					String ref = beanReference.getMappingBean()[0];
					parameter[len] = getBeanDefinition(ref).getStatus().equals(BeanStatus.INIT) ? null : getBean(ref);
					parameterType[len] = Class.forName(getBeanDefinition(ref).getClassName());
					len++;
				} else if (LIST.equals(beanReference.getType())) {
					List<Object> list = new ArrayList<Object>();
					solveCollection(list, beanReference);
					parameterType[len] = List.class;
					parameter[len++] = list;
				} else if (SET.equals(beanReference.getType())) {
					Set<Object> set = new HashSet<Object>();
					solveCollection(set, beanReference);
					parameterType[len] = Set.class;
					parameter[len++] = set;
				}
			} else {
				parameterType[len] = pv.getValue().getClass();
				parameter[len++] = pv.getValue();
			}
		}
		try {
			Constructor constructor = clazz.getConstructor(parameterType);
			result = constructor.newInstance(parameter);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public Object getAnnotationBeanInstance(List<AnnotationMeta> annotationMetas) {
		Object result = null;
		for (AnnotationMeta annotationMeta : annotationMetas) {
			try {
				result = getAnnotationBeanInstance(annotationMeta);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public Object getAnnotationBeanInstance(AnnotationMeta annotationMeta) throws Exception {
		Object result = null;
		Constructor constructor = (Constructor) annotationMeta.getTarget();
		constructor.setAccessible(true);
		Object[] parameter = new Object[constructor.getParameterTypes().length];
		Set<Annotation> annotationSet = annotationMeta.getAnnotation();
		if (annotationSet == null || annotationSet.isEmpty()) {
			return null;
		}
		int i = 0;
		for (Annotation annotation : annotationSet) {
			if (i == parameter.length) {
				break;
			}
			parameter[i] = getValueBySupportAnnotations(constructor.getParameterTypes()[i], annotation);
			i++;
		}
		try {
			result = constructor.newInstance(parameter);
		} catch (InstantiationException e) {
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
		} finally {
			constructor.setAccessible(false);
		}
		return result;
	}

	public void populateBean(Object obj, BeanDefinition beanDefinition) {
		if (beanDefinition instanceof SimpleBeanDefinition) {
			autowireSimpleBean(obj, (SimpleBeanDefinition) beanDefinition);
		} else if (beanDefinition instanceof AnnotationBeanDefinition) {
			try {
				autowireAnnotationBean(obj, (AnnotationBeanDefinition) beanDefinition);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 装配注解的Bean
	 * 
	 * @param obj
	 * @param beanDefinition
	 * @throws Exception 
	 */
	public void autowireAnnotationBean(Object obj, AnnotationBeanDefinition beanDefinition) throws Exception {
		List<AnnotationMeta> annotationMetas = beanDefinition.getAnnotationMeta();
		
		if (annotationMetas == null || annotationMetas.isEmpty()) {
			return;
		}
		for (AnnotationMeta annotationMeta : annotationMetas) {
			if (AnnotationMetaType.FIELD.equals(annotationMeta.getAnnotationType())) {
				try {
					autowireField(obj, annotationMeta);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (AnnotationMetaType.METHOD.equals(annotationMeta.getAnnotationType())) {
				autowireMethod(obj, annotationMeta);
			}
		}

	}

	public void autowireField(Object obj, AnnotationMeta annotationMeta) throws Exception {
		Field field = (Field) annotationMeta.getTarget();
		field.setAccessible(true);
		Object fieldValue = null;
		Set<Annotation> annotationSet = annotationMeta.getAnnotation();
		for (Annotation annotation : annotationSet) {
			String annotationTypeName = annotation.annotationType().getSimpleName();
			if (AnnotationType.AUTOWIRED.getName().equals(annotationTypeName)
					|| AnnotationType.VALUE.getName().equals(annotationTypeName)) {
				fieldValue = getValueBySupportAnnotations(field.getType(), annotation);
				if (fieldValue == null) {
					continue;
				}
				try {
					field.set(obj, fieldValue);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		field.setAccessible(false);
	}

	@SuppressWarnings("rawtypes")
	public Object getValueBySupportAnnotations(Class clazz, Annotation annotation) throws Exception {
		Object result = null;
		if (AnnotationType.AUTOWIRED.getName().equals(annotation.annotationType().getSimpleName())) {
			Autowired autowried = (Autowired) annotation;
			String autowriedValue = autowried.vlaue();
			if (autowriedValue == null || "".equals(autowriedValue)) {
				result = autowiredByType(clazz);
			} else {

				result = (getBeanDefinition(autowriedValue).getStatus().equals(BeanStatus.INIT)) ? null
						: getBean(autowriedValue);
			}
		} else if (AnnotationType.VALUE.getName().equals(annotation.annotationType().getSimpleName())) {
			Value value = (Value) annotation;
			result = value.value();
		}
		return result;
	}

	/**
	 * 根据方法上的注解的装配对象
	 * 
	 * @param obj
	 * @param annotationMeta
	 * @throws Exception 
	 */
	public void autowireMethod(Object obj, AnnotationMeta annotationMeta) throws Exception {
		Method method = (Method) annotationMeta.getTarget();
		method.setAccessible(true);
		if (method.getParameterTypes().length == 0) {
			return;
		}
		Object[] parameters = new Object[method.getParameterTypes().length];
		int i = 0;
		Set<Annotation> annotationSet = annotationMeta.getAnnotation();
		for (Annotation annotation : annotationSet) {
			if (i == parameters.length) {
				break;
			}
			String annotationTypeName = annotation.annotationType().getName();
			if (AnnotationType.AUTOWIRED.getName().equals(annotationTypeName)
					|| AnnotationType.VALUE.getName().equals(annotationTypeName)) {
				parameters[i] = getValueBySupportAnnotations(method.getParameterTypes()[i], annotation);
				i++;
			}
		}
		try {
			method.invoke(obj, parameters);
			method.setAccessible(false);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object autowiredByType(Class clazz) throws Exception {
		boolean isAbstract=Modifier.isAbstract(clazz.getModifiers());
		if(clazz.isInterface()||isAbstract){
			List<String> list=getSubClass(clazz.getName());
			if(list==null||list.isEmpty()){
				throw new Exception("该接口或者抽象类无子类");
			}else if(list.size()>1){
				throw new Exception("该接口或者抽象类的子类超过一个，无法选择那一个装配");
			}else{
				clazz=Class.forName(list.get(0));
			}
		}
		Component annotation = (Component) clazz.getAnnotation(Component.class);
		if (annotation == null) {
			return null;
		}
		String beanName = "".equals(annotation.value()) ? clazz.getSimpleName() : annotation.value();
		if (getBeanDefinition(beanName).getStatus().equals(BeanStatus.INIT)) {
			return null;
		}
		return getBean(beanName);
	}

	public void autowireSimpleBean(Object obj, SimpleBeanDefinition beanDefinition) {
		for (PropertyValue pv : beanDefinition.getPvs().getPropertyValues()) {
			if (pv.getValue() instanceof BeanReference) {
				BeanReference beanReference = (BeanReference) pv.getValue();

				if (OBJECT.equals(beanReference.getType())) {
					String ref = beanReference.getMappingBean()[0];
					if (getBeanDefinition(ref).getStatus().equals(BeanStatus.INIT)) {
						continue;
					}
					this.invoke(obj, pv.getName(), getBean(ref));
				} else if (LIST.equals(beanReference.getType())) {
					this.invokeList(obj, pv.getName(), beanReference);
				} else if (SET.equals(beanReference.getType())) {
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
			} else if (REF.equals(mappingType[i]) && !getBeanDefinition(mappingBean[i]).equals(BeanStatus.INIT)) {
				collection.add(getBean(mappingBean[i]));
			}
		}
		this.invoke(obj, fieldName, collection);

	}

	public void solveCollection(Collection<Object> collection, BeanReference beanReference) {
		String[] mappingBean = beanReference.getMappingBean();
		String[] mappingType = beanReference.getMappingType();
		for (int i = 0; i < mappingBean.length; i++) {
			if (VALUE.equals(mappingType[i])) {
				collection.add(mappingBean[i]);
			} else if (REF.equals(mappingType[i]) && !getBeanDefinition(mappingBean[i]).equals(BeanStatus.INIT)) {
				collection.add(getBean(mappingBean[i]));
			}
		}
	}

	public abstract BeanDefinition getBeanDefinition(String name);
	public abstract List<String> getSubClass(String name);
}
