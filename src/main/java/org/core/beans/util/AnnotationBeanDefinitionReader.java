package org.core.beans.util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.core.beans.AbstractBeanDefinitionReader;
import org.core.beans.AnnotationBeanDefinition;
import org.core.beans.AnnotationMeta;
import org.core.beans.constant.AnnotationMetaType;
import org.core.beans.constant.AnnotationType;
import org.core.beans.constant.BeanStatus;
import org.core.beans.factory.support.BeanDefinitionRegistry;
import org.core.beans.io.Resource;
import org.core.beans.factory.annotation.Component;

public class AnnotationBeanDefinitionReader extends AbstractBeanDefinitionReader {
	public final Map<String, Boolean> supportAnnotation = new ConcurrentHashMap<String, Boolean>(16);

	public AnnotationBeanDefinitionReader() {
		super(null);
	}

	public AnnotationBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
		super(beanDefinitionRegistry);
		for (AnnotationType annotation : AnnotationType.values()) {
			supportAnnotation.put(annotation.getName(), true);
		}
	}

	public int loadBeanDefinitions(Resource resource) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int loadBeanDefinitions(Resource... resources) {
		// TODO Auto-generated method stub

		return 0;
	}

	public int scan(String... packages) {
		for (String packageStr : packages) {
			scanPackage(packageStr);
		}
		return 0;
	}

	public int scanPackage(String packageName) {
		String packageDir = packageName.replace('.', '/');
		URL url = Thread.currentThread().getContextClassLoader().getResource(packageDir);
		File file = new File(url.getFile());
		travelAllClazz(file, packageDir);
		return 0;
	}

	public void travelAllClazz(File root, String packageName) {
		if (root.isFile() && root.getName().endsWith(".class")) {
			String clazzName = root.getName().substring(0, root.getName().length() - ".class".length());
			clazzName = packageName.replace("/", ".") + "." + clazzName;
			scanClass(clazzName);
		}
		if (root.isDirectory()) {
			File[] children = root.listFiles();
			for (File child : children) {
				if (child.isDirectory()) {
					packageName += "/" + child.getName();
				}
				travelAllClazz(child, packageName);

			}
		}

	}

	@SuppressWarnings("rawtypes")
	public int scanClass(String className) {
		try {
			Class clazz = Class.forName(className);
			String beanName = clazz.getSimpleName();
			Annotation[] clazzAnnotations = clazz.getAnnotations();
			for (Annotation annotation : clazzAnnotations) {
				if (annotation.annotationType().isAssignableFrom(Component.class)) {
					Component component = (Component) annotation;
					beanName = component.value().equals("") ? clazz.getSimpleName() : component.value();
					// System.out.println(clazz.getName());
				}
				// System.out.println("-------------"+annotation.annotationType().getName());
			}

			AnnotationBeanDefinition annotationBeanDefinition = new AnnotationBeanDefinition();
			List<AnnotationMeta> annotationMetaList = new ArrayList<AnnotationMeta>();
			List<AnnotationMeta> constructorMetaList = new ArrayList<AnnotationMeta>();
			this.scanField(annotationMetaList, clazz);
			// System.out.println("after scan
			// "+className+"="+annotationMetaList.size());
			this.scanConstructor(constructorMetaList, clazz);
			this.scanMethod(annotationMetaList, clazz);
			annotationBeanDefinition.setAnnotationMeta(annotationMetaList);
			annotationBeanDefinition.setConstructorMeta(constructorMetaList);
			annotationBeanDefinition.setClassName(clazz.getName());
			annotationBeanDefinition.setScope("singleton");
			annotationBeanDefinition.setStatus(BeanStatus.PROPROCEING);
		    boolean isAbstract=Modifier.isAbstract(clazz.getModifiers());
		    if(!clazz.isInterface()&&!isAbstract){
		    	getBeanDefinitionRegistry().setExtendsMap(clazz);
		    }
			getBeanDefinitionRegistry().registryBeanDefinition(beanName, annotationBeanDefinition);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 扫描字段上的注解（将支持的注解记录下来）
	 * 
	 * @param list
	 * @param clazz
	 */
	@SuppressWarnings("rawtypes")
	protected void scanField(List<AnnotationMeta> list, Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		scanMember(list, fields, AnnotationMetaType.FIELD);
	}

	/**
	 * 扫描方法上的注解
	 * 
	 * @param list
	 * @param clazz
	 */
	@SuppressWarnings("rawtypes")
	protected void scanMethod(List<AnnotationMeta> list, Class clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		scanMember(list, methods, AnnotationMetaType.METHOD);

	}

	/**
	 * 扫描构造方法的注解
	 * 
	 * @param list
	 * @param clazz
	 */
	@SuppressWarnings("rawtypes")
	protected void scanConstructor(List<AnnotationMeta> list, Class clazz) {
		Constructor[] constructors = clazz.getConstructors();
		scanMember(list, constructors, AnnotationMetaType.CONSTRUCTOR);
	}

	@SuppressWarnings("rawtypes")
	protected void scanMember(List<AnnotationMeta> list, Object[] objects, AnnotationMetaType type) {
		boolean first = true;
		for (Object obj : objects) {
			Annotation[] annotations = null;
			if (obj instanceof Field) {
				((Field) obj).setAccessible(true);
				annotations = ((Field) obj).getAnnotations();
			} else if (obj instanceof Method) {
				((Method) obj).setAccessible(true);
				annotations = ((Method) obj).getAnnotations();
			} else if (obj instanceof Constructor) {
				((Constructor) obj).setAccessible(true);
				annotations = ((Constructor) obj).getAnnotations();
			}
			if (annotations == null) {
				continue;
			}
			AnnotationMeta meta = null;
			Set<Annotation> annotationSet = new HashSet<Annotation>();
			for (Annotation annotation : annotations) {
				if (supportAnnotation.containsKey(annotation.annotationType().getSimpleName())) {
					if (first) {
						meta = new AnnotationMeta();
						meta.setTarget(obj);
						meta.setAnnotationType(type);
						first = false;
					}
					annotationSet.add(annotation);
				}
			}
			if (meta != null) {
				meta.setAnnotation(annotationSet);
				list.add(meta);
			}
			first = true;
			if (obj instanceof Field) {
				((Field) obj).setAccessible(false);
			} else if (obj instanceof Method) {
				((Method) obj).setAccessible(false);
			} else if (obj instanceof Constructor) {
				((Constructor) obj).setAccessible(false);
			}
		}

	}

}
