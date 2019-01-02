package com.orm.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * mybatis配置上下文对象
 * 
 * @author 紫马
 *
 */
public class HibernateCfg implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4648359383771087478L;
	private String driver_class;
	private String username;
	private String password;
	private String url;
	private String show_sql;
	private String format_sql;
	private String hbm2ddl_auto;
	private String dialect;
	private String autocommit;
	private Map<String,Mapping> mappings;
	public String getDriver_class() {
		return driver_class;
	}
	public void setDriver_class(String driver_class) {
		this.driver_class = driver_class;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getShow_sql() {
		return show_sql;
	}
	public void setShow_sql(String show_sql) {
		this.show_sql = show_sql;
	}
	public String getFormat_sql() {
		return format_sql;
	}
	public void setFormat_sql(String format_sql) {
		this.format_sql = format_sql;
	}
	public String getHbm2ddl_auto() {
		return hbm2ddl_auto;
	}
	public void setHbm2ddl_auto(String hbm2ddl_auto) {
		this.hbm2ddl_auto = hbm2ddl_auto;
	}
	public String getDialect() {
		return dialect;
	}
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
	public String getAutocommit() {
		return autocommit;
	}
	public void setAutocommit(String autocommit) {
		this.autocommit = autocommit;
	}
	public Map<String, Mapping> getMappings() {
		return mappings;
	}
	public void setMappings(Map<String, Mapping> mappings) {
		this.mappings = mappings;
	}
}
