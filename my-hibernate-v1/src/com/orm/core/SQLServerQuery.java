package com.orm.core;

import java.util.List;

public class SQLServerQuery<T> implements Query<T>{

	public SQLServerQuery(Session session) {
		super();
	}
	public SQLServerQuery(Session session, String hql, Class<?> clazz) {
		super();
	}
	@Override
	public T get(Class<T> clazz, Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParameter(String key, Object value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<T> list() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
