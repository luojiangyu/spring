package org.core.beans;

import org.core.beans.constant.BeanStatus;

public class SimpleBeanDefinition implements BeanDefinition{
	private Object beanClass;
	private String ClassName;
	private String scope;
	private PropertyValues pvs;
	private PropertyValues constructorArgs;//构造参数
	private BeanStatus status;
	public Object getBeanClass() {
		return beanClass;
	}
	public void setBeanClass(Object beanClass) {
		this.beanClass = beanClass;
	}
	public String getClassName() {
		return ClassName;
	}
	public void setClassName(String className) {
		ClassName = className;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public PropertyValues getPvs() {
		return pvs;
	}
	public void setPvs(PropertyValues pvs) {
		this.pvs = pvs;
	}
	public BeanStatus getStatus() {
		return status;
	}
	public void setStatus(BeanStatus status) {
		this.status = status;
	}
	public PropertyValues getConstructorArgs() {
		return constructorArgs;
	}
	public void setConstructorArgs(PropertyValues constructorArgs) {
		this.constructorArgs = constructorArgs;
	}

	
}
