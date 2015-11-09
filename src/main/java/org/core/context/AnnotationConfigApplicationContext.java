package org.core.context;


import org.core.beans.factory.DefaultListableBeanFactory;
import org.core.beans.util.AnnotationBeanDefinitionReader;

public class AnnotationConfigApplicationContext extends AbstractRefreshableApplicationContext{
   public  AnnotationConfigApplicationContext(String configLocation){
	   this(false,new String[]{configLocation});
   }
   public AnnotationConfigApplicationContext(boolean refresh,String configLocation){
	   this(refresh,new String[]{configLocation});
   }
   public AnnotationConfigApplicationContext(boolean refresh,String ... configLocations){
	   super();
	   try{
		   setConfigLocations(configLocations);
		   if(refresh){
			   refresh();
		   }
	   }catch(Exception e){
		   e.printStackTrace();
	   }
   }
   public void loadBeanDefinitions(DefaultListableBeanFactory beanFactory){
	   AnnotationBeanDefinitionReader reader =new AnnotationBeanDefinitionReader(beanFactory);
	   reader.scan(getConfigLocations());
   }
   
   
   
}
