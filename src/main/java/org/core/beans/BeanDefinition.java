package org.core.beans;

import org.core.beans.constant.BeanStatus;

public interface BeanDefinition {
	public String getScope();
	public String getClassName();
	public Object getBeanClass();
	public BeanStatus getStatus();
	public void setStatus(BeanStatus status);
	public void setBeanClass(Object beanClass);

}
