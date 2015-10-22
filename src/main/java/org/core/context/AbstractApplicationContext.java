package org.core.context;

import org.core.beans.factory.DefaultListableBeanFactory;

public abstract class AbstractApplicationContext implements ApplicationContext{
      public Object getBean(String name){
    	  return getBeanFactory().getBean(name);
      }
      public boolean containsBean(String name){
    	  return getBeanFactory().containsBean(name);
      }
      public void refresh(){
    	  obtainBeanFactory();
      }
      public abstract DefaultListableBeanFactory obtainBeanFactory();
      public abstract DefaultListableBeanFactory getBeanFactory();
}
