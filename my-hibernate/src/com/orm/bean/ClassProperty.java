package com.orm.bean;

public class ClassProperty {
	private String name;
	private String column;
	private String generator;
	private String jdbcType;
	public ClassProperty() {
		super();
	}
	
	public ClassProperty(String name, String column,String jdbcType) {
		super();
		this.name = name;
		this.column = column;
		this.jdbcType = jdbcType;
	}

	public String getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getGenerator() {
		return generator;
	}
	public void setGenerator(String generator) {
		this.generator = generator;
	}
	
}
