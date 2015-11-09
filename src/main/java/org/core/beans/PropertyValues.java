package org.core.beans;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {
	private  List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
	public void setPropertyValues(List<PropertyValue> propertyValues){
		this.propertyValues=propertyValues;
	}
	public void addPropertyValue(PropertyValue pv){
		this.propertyValues.add(pv);
	}
	public List<PropertyValue> getPropertyValues(){
		return this.propertyValues;
	}
}
