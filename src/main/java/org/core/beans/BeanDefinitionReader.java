package org.core.beans;

import org.core.beans.io.Resource;

public interface BeanDefinitionReader {
    int loadBeanDefinitions(Resource resource);
    int loadBeanDefinitions(Resource... resources);
}
