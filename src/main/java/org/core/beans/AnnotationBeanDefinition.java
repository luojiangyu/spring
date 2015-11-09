package org.core.beans;

import java.util.List;

import org.core.beans.constant.BeanStatus;

public class AnnotationBeanDefinition implements BeanDefinition {
   private String scope;
   private String ClassName;
   private Object BeanClass;
   private BeanStatus status;
   private List<AnnotationMeta> annotationMeta;
   private List<AnnotationMeta> constructorMeta;//构造方法注解
public String getScope() {
	return scope;
}
public void setScope(String scope) {
	this.scope = scope;
}
public String getClassName() {
	return ClassName;
}
public void setClassName(String className) {
	ClassName = className;
}
public Object getBeanClass() {
	return BeanClass;
}
public void setBeanClass(Object beanClass) {
	BeanClass = beanClass;
}

public BeanStatus getStatus() {
	return status;
}
public void setStatus(BeanStatus status) {
	this.status = status;
}
public List<AnnotationMeta> getAnnotationMeta() {
	return annotationMeta;
}
public void setAnnotationMeta(List<AnnotationMeta> annotationMeta) {
	this.annotationMeta = annotationMeta;
}
public List<AnnotationMeta> getConstructorMeta() {
	return constructorMeta;
}
public void setConstructorMeta(List<AnnotationMeta> constructorMeta) {
	this.constructorMeta = constructorMeta;
}

   

}
