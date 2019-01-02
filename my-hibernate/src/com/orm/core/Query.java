package com.orm.core;

import java.util.List;

public interface Query<T> {
	
	public T get(Class<T> clazz,Integer id);
	
	public void setParameter(String key,Object value);

	public List<T> list();
}
