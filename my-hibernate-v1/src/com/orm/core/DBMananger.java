package com.orm.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.orm.bean.HibernateCfg;

/**
 * 根据配置文件，管理连接对象
 * 
 * @author 紫马
 *
 */
public class DBMananger {

	public static Connection getConnection(HibernateCfg hibernateCfg) {
		try {
			Class.forName(hibernateCfg.getDriver_class());
			return DriverManager.getConnection(hibernateCfg.getUrl(),hibernateCfg.getUsername(), hibernateCfg.getPassword());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void closeConnection(Connection conn, PreparedStatement ps) {
		try {
			if (conn != null) {
				conn.close();
			}
			if (ps != null) {
				ps.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
