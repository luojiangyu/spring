package org.core.beans.constant;
public enum AnnotationType {
   AUTOWIRED("Autowired"),VALUE("Value"),COMPONENT("Component");
	private String name;
	private AnnotationType(String name){
		this.name=name;
	}
	public String getName(){
		return this.name;
	}
}
