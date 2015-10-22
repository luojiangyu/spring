package org.core.beans.factory;

public interface BeanFactory {
    public Object getBean(String name);
    public boolean containsBean(String name);
}
