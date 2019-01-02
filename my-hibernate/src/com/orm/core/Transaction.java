package com.orm.core;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction {
	
	private Connection connection;
	
	public Transaction(Connection connection) {
		super();
		this.connection = connection;
	}

	public void commit() {
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void rollBack() {
		try {
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
