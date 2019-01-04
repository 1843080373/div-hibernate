package com.orm.bean;

import java.util.LinkedList;

public class Mapping {

	private String mapping_package;
	private String mapping_clazz;
	private String table;
	private ClassProperty id;
	private LinkedList<ClassProperty> commonProperty;
	public String getMapping_package() {
		return mapping_package;
	}
	public void setMapping_package(String mapping_package) {
		this.mapping_package = mapping_package;
	}
	public String getMapping_clazz() {
		return mapping_clazz;
	}
	public void setMapping_clazz(String mapping_clazz) {
		this.mapping_clazz = mapping_clazz;
	}
	
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public ClassProperty getId() {
		return id;
	}
	public void setId(ClassProperty id) {
		this.id = id;
	}
	public LinkedList<ClassProperty> getCommonProperty() {
		return commonProperty;
	}
	public void setCommonProperty(LinkedList<ClassProperty> commonProperty) {
		this.commonProperty = commonProperty;
	}
	public String getTableColum(String name) {
		LinkedList<ClassProperty> commonProperty=this.commonProperty;
		commonProperty.add(id);
		for (ClassProperty classProperty : commonProperty) {
			if(name.equals(classProperty.getName())) {
				return classProperty.getColumn();
			}
		}
		return null;
	}
}
