package org.core.beans;

public class BeanDefinition {
	private Object beanClass;
	private String ClassName;
	private String scope;
	private PropertyValues pvs;
	private String status;//status:0：预处理中，1：getBean中，2：getBean完成。
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
