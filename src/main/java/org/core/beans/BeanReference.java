package org.core.beans;

public class BeanReference {
	private String[] MappingBean;
	private String[] MappingType;
	private String Type;

	public String[] getMappingBean() {
		return MappingBean;
	}

	public void setMappingBean(String[] mappingBean) {
		MappingBean = mappingBean;
	}

	public String[] getMappingType() {
		return MappingType;
	}

	public void setMappingType(String[] mappingType) {
		MappingType = mappingType;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

}
