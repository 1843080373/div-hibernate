package com.orm.core;

import java.sql.Connection;

import com.orm.bean.HibernateCfg;

public class SessionFactory {
	
	private HibernateCfg hibernateCfg;
	
	private Connection connection;
	
	public SessionFactory(HibernateCfg hibernateCfg) {
		this.hibernateCfg = hibernateCfg;
	}

	public void close() {
        DBMananger.closeConnection(connection, null);
	}

	public Session getCurrentSession() {
		connection=DBMananger.getConnection(hibernateCfg);
		return new Session(hibernateCfg, connection);
	}

}
