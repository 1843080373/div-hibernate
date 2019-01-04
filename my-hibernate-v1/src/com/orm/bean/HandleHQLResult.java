package com.orm.bean;

import java.util.LinkedList;

public class HandleHQLResult {
	private String tableName;
	private String psSQL;
	private LinkedList<Param> params=new LinkedList<>();
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public LinkedList<Param> getParams() {
		return params;
	}
	public void setParams(LinkedList<Param> params) {
		this.params = params;
	}
	public String getPsSQL() {
		return psSQL;
	}
	public void setPsSQL(String psSQL) {
		this.psSQL = psSQL;
	}
	public Param getParam(String key) {
		for (Param param : params) {
			if(key.equals(param.getName())) {
				return param;
			}
		}
		return null;
	}
}
