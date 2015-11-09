package org.core.context;

import org.core.beans.factory.DefaultListableBeanFactory;
import org.core.beans.io.DefaultResourceLoader;
import org.core.beans.util.XmlBeanDefinitionReader;

public class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

    public void loadBeanDefinitions(DefaultListableBeanFactory beanFactory){
    	XmlBeanDefinitionReader beanDefinitionReader=new XmlBeanDefinitionReader(beanFactory);
    	beanDefinitionReader.setResourceLoader(new DefaultResourceLoader());
    	loadBeanDefinitions(beanDefinitionReader);
    }
    public void loadBeanDefinitions(XmlBeanDefinitionReader reader){
    	String[] configLocations = getConfigLocations();
    	if(configLocations!=null){
    		reader.loadBeanDefinitions(configLocations);
    	}
    	
    }

	
}
