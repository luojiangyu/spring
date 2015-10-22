package org.core.beans;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {
	public final List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
	public void addPropertyValue(PropertyValue pv){
		this.propertyValues.add(pv);
	}
	public List<PropertyValue> getPropertyValues(){
		return this.propertyValues;
	}
}
