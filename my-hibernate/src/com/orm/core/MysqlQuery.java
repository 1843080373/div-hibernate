package com.orm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.orm.bean.ClassProperty;
import com.orm.bean.HandleHQLResult;
import com.orm.bean.Mapping;
import com.orm.bean.Param;
import com.orm.utils.ReflectUtils;
import com.orm.utils.StringUtils;

public class MysqlQuery<T> implements Query<T>{

	private Session session;
	private Class<?> clazz;
	private HandleHQLResult handleHQLResult;
	private PreparedStatement ps = null;
	public MysqlQuery(Session session) {
		super();
		this.session = session;
	}
	public MysqlQuery(Session session, String hql, Class<?> clazz) {
		super();
		this.session = session;
		this.clazz = clazz;
		this.handleHQLResult=handlePSSQL(hql);
		Connection connection = session.getConnection();
		try {
			ps = connection.prepareStatement(handleHQLResult.getPsSQL());
			if (Boolean.valueOf(session.getHibernateCfg().getShow_sql())) {
				System.out.println(handleHQLResult.getPsSQL());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private HandleHQLResult handlePSSQL(String hql) {
		try {
			HandleHQLResult handleHQLResult = new HandleHQLResult();
			Pattern p1 = Pattern.compile("(?<=\\s)[^\\s\\:]+(?=\\:)");
			Pattern p2 = Pattern.compile(":[\\S]+");
			Matcher m1 = p1.matcher(hql);
			StringBuilder psBuilder = new StringBuilder("SELECT * FROM ");
			int i = 1;
			LinkedList<Param> params = handleHQLResult.getParams();
			while (m1.find()) {
				String item = hql.substring(m1.start(), m1.end());
				Param param = new Param();
				param.setProperties(item.substring(0, item.length() - 1));
				param.setPos(i);
				params.add(param);
				i++;
			}
			Matcher m2 = p2.matcher(hql);
			int j=0;
			String rgex = "from(.*?)where";
			handleHQLResult.setTableName(StringUtils.getSubUtilSimple(hql, rgex));
			Mapping mapping = session.getHibernateCfg().getMappings().get(clazz.getName());
			psBuilder.append(mapping.getTable());
			psBuilder.append(" WHERE 1=1");
			while (m2.find()) {
				String properties = hql.substring(m2.start(), m2.end());
				params.get(j).setName(properties.substring(1));
				psBuilder.append(" AND "+mapping.getTableColum(params.get(j).getProperties())+"=?");
				j++;
			}
			handleHQLResult.setPsSQL(psBuilder.toString());
			return handleHQLResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public T get(Class<T> clazz, Integer id) {
		this.clazz=clazz;
		Connection connection = session.getConnection();
		try {
			PreparedStatement ps = null;
			Mapping mapping = session.getHibernateCfg().getMappings().get(clazz.getName());
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM ");
			sb.append(mapping.getTable());
			sb.append(" WHERE ");
			ClassProperty idProperty = mapping.getId();
			sb.append(idProperty.getColumn() + " =? ");
			ps = connection.prepareStatement(sb.toString());
			ps.setObject(1, id);
			if (Boolean.valueOf(session.getHibernateCfg().getShow_sql())) {
				System.out.println(sb);
			}
			ResultSet rs = ps.executeQuery();
			return ((List<T>)buildResultSet(rs,mapping)).get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private List<T> buildResultSet(ResultSet rs,Mapping mapping) {
		List<T> list=new ArrayList<>();
		try {
			TypeConvertor convertor=new MySQLTypeConvertor();
			while (rs.next()) {
				Object item= clazz.newInstance();
				List<ClassProperty> results = mapping.getCommonProperty();
				for (ClassProperty result : results) {
					 buildColumn(rs, convertor, item, result);
				}
				ClassProperty id=mapping.getId();
				buildColumn(rs, convertor, item, id);
				list.add((T) item);
			}
		} catch (SQLException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public void buildColumn(ResultSet rs, TypeConvertor convertor, Object item, ClassProperty result)
			throws SQLException {
		 String column=result.getColumn();
		 String property=result.getName();
		 String jdbcType=result.getJdbcType();
		 ReflectUtils.invokeSet(item, property, convertor.dbTypeToJavaType(jdbcType), rs.getObject(column));
	}
	
	@Override
	public void setParameter(String key, Object value) {
		Param param=handleHQLResult.getParam(key);
		if(param!=null) {
			try {
				ps.setObject(param.getPos(), value);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public List<T> list() {
		try {
			ResultSet rs = ps.executeQuery();
			Mapping mapping = session.getHibernateCfg().getMappings().get(clazz.getName());
			return ((List<T>)buildResultSet(rs,mapping));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
