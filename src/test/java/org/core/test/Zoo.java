package org.core.test;

import java.util.List;
import java.util.Set;

public class Zoo {
   private List<Animal> animals;
   private Set<String> text;
   public void showAnimals(){
	   for(Animal animal:animals){
		   animal.saySomething();
	   }
	   for(String temp:text){
		   System.out.println(temp);
	   }
   }
   public void setAnimals(List<Animal> animals){
	   this.animals = animals;
   }
   public void setText(Set<String> text){
	   this.text=text;
   }
}
