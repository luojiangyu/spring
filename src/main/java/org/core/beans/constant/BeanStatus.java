package org.core.beans.constant;

public enum BeanStatus {
	 PROPROCEING(0,"预处理中"),INIT(2,"实例化Bean中"),AVAILABLE(3,"Bean可用");
	private int status;
	private String desc;
	private BeanStatus(int status,String desc){
		this.status=status;
		this.desc=desc;
	}
	public int getStatus(){
		return this.status;
	}
	public String getDesc(){
		return this.desc;
	}
}
