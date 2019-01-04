package com.orm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.orm.bean.ClassProperty;
import com.orm.bean.HibernateCfg;
import com.orm.bean.Mapping;
import com.orm.utils.ReflectUtils;

public class Session {

	private HibernateCfg hibernateCfg;

	private Connection connection;

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public HibernateCfg getHibernateCfg() {
		return hibernateCfg;
	}

	public void setHibernateCfg(HibernateCfg hibernateCfg) {
		this.hibernateCfg = hibernateCfg;
	}

	public Session(HibernateCfg hibernateCfg, Connection connection) {
		super();
		this.hibernateCfg = hibernateCfg;
		this.connection = connection;
	}

	public Transaction getTransaction() {
		return new Transaction(connection);
	}

	public Transaction beginTransaction() {
		try {
			connection.setAutoCommit(false);
			return new Transaction(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public <T> Query<T>  createQuery() {
		if("org.hibernate.dialect.MySQLDialect".equals(hibernateCfg.getDialect())) {
			return (Query<T>) new MysqlQuery<T>(this);
		}
		return (Query<T>) new SQLServerQuery<T>(this);
	}

	public <T> Query<T>  createQuery(String hql,Class<T> clazz) {
		if("org.hibernate.dialect.MySQLDialect".equals(hibernateCfg.getDialect())) {
			return (Query<T>) new MysqlQuery<T>(this, hql,clazz);
		}
		return (Query<T>) new SQLServerQuery<T>(this, hql,clazz);
	}

	public void save(Object o) {
		createDDL(Opreate.INSERT, o);
	}

	public void update(Object o) {
		createDDL(Opreate.UPDATE, o);
	}

	public void delete(Class<?> clazz,Integer id) {
		createDDL(clazz,id);
	}

	private void createDDL(Class<?> clazz, Integer id) {
		try {
			PreparedStatement ps = null;
			Mapping mapping = hibernateCfg.getMappings().get(clazz.getName());
			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM ");
			sb.append(mapping.getTable());
			sb.append(" WHERE ");
			ClassProperty idProperty = mapping.getId();
			sb.append(idProperty.getColumn() + " =? ");
			ps = connection.prepareStatement(sb.toString());
			ps.setObject(1, id);
			if (Boolean.valueOf(hibernateCfg.getShow_sql())) {
				System.out.println("sql:"+sb);
			}
			System.out.println("param:"+id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createDDL(Opreate opreate, Object o) {
		try {
			PreparedStatement ps = null;
			Class<? extends Object> oClass = o.getClass();
			Mapping mapping = hibernateCfg.getMappings().get(oClass.getName());
			if(mapping==null) {
				throw new RuntimeException("非持久化对象");
			}
			StringBuilder sb = new StringBuilder();
			if (Opreate.INSERT == opreate) {
				sb.append("INSERT INTO ");
				ClassProperty id = mapping.getId();
				boolean isNative = "native".equals(id.getGenerator());
				sb.append(mapping.getTable() + " (");
				if (isNative) {
					sb.append(id.getColumn() + ",");
				}
				List<ClassProperty> commonProperty = mapping.getCommonProperty();
				for (ClassProperty classProperty : commonProperty) {
					sb.append(classProperty.getColumn() + ",");
				}
				sb = sb.deleteCharAt(sb.length() - 1);
				sb.append(" ) VALUES (");
				if (isNative) {
					sb.append("?,");
				}
				for (int i = 0; i < commonProperty.size(); i++) {
					sb.append("?,");
				}
				sb = sb.deleteCharAt(sb.length() - 1);
				sb.append(")");
				ps = connection.prepareStatement(sb.toString());
				if (isNative) {
					Object value = ReflectUtils.invokeGet(o, id.getName());
					ps.setObject(1, value);
				}
				for (int i = 0; i < commonProperty.size(); i++) {
					ClassProperty clazzP = commonProperty.get(i);
					Object value = ReflectUtils.invokeGet(o, clazzP.getName());
					if (!isNative) {
						ps.setObject(i + 1, value);
					} else {
						ps.setObject(i + 2, value);
					}
				}
			} else if (Opreate.UPDATE == opreate) {
				sb.append("UPDATE ");
				sb.append(mapping.getTable() + " SET ");
				List<ClassProperty> commonProperty = mapping.getCommonProperty();
				for (ClassProperty classProperty : commonProperty) {
					sb.append(classProperty.getColumn() + "=?,");
				}
				sb = sb.deleteCharAt(sb.length() - 1);
				sb.append(" WHERE ");
				ClassProperty id = mapping.getId();
				sb.append(id.getColumn() + " =? ");
				ps = connection.prepareStatement(sb.toString());
				Object value = ReflectUtils.invokeGet(o, id.getName());
				ps.setObject(1, value);
				for (int i = 0; i < commonProperty.size(); i++) {
					ClassProperty clazzP = commonProperty.get(i);
					Object value1 = ReflectUtils.invokeGet(o, clazzP.getName());
					ps.setObject(i + 1, value1);
				}
				ps.setObject(commonProperty.size() + 1, ReflectUtils.invokeGet(o, id.getName()));
			} else if (Opreate.DELEET == opreate) {
				sb.append("DELETE ");
				sb.append(" WHERE ");
				ClassProperty id = mapping.getId();
				sb.append(id.getColumn() + " =? ");
				ps = connection.prepareStatement(sb.toString());
				ps.setObject(1, ReflectUtils.invokeGet(o, id.getName()));
			}
			if (Boolean.valueOf(hibernateCfg.getShow_sql())) {
				System.out.println("sql:"+sb);
			}
			System.out.println("param:"+JSONObject.toJSONString(o));
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
