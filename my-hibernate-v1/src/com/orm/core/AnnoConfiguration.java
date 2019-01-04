package com.orm.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.orm.bean.ClassProperty;
import com.orm.bean.HibernateCfg;
import com.orm.bean.Mapping;
import com.orm.core.enums.Column;
import com.orm.core.enums.Entity;
import com.orm.core.enums.Id;
import com.orm.core.enums.Table;
@SuppressWarnings("unchecked")
public class AnnoConfiguration {

	private static HibernateCfg hibernateCfg;

	public AnnoConfiguration configure() {
		InputStream inputStream = null;
		try {
			hibernateCfg = new HibernateCfg();
			SAXReader reader = new SAXReader();
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("hibernate.cfg.xml");
			Document document = reader.read(inputStream);
			Element root = document.getRootElement();
			// session-factory
			Element sessionFactoryElements = root.element("session-factory");
			if (sessionFactoryElements != null) {
				// property
				hibernateCfg.setDriver_class(
						sessionFactoryElements.selectSingleNode("property[@name='hibernate.connection.driver_class']").getText());
				hibernateCfg.setUsername(
						sessionFactoryElements.selectSingleNode("property[@name='hibernate.connection.username']").getText());
				hibernateCfg.setPassword(
						sessionFactoryElements.selectSingleNode("property[@name='hibernate.connection.password']").getText());
				hibernateCfg.setUrl(sessionFactoryElements.selectSingleNode("property[@name='hibernate.connection.url']").getText());
				hibernateCfg.setShow_sql(sessionFactoryElements.selectSingleNode("property[@name='show_sql']").getText());
				hibernateCfg.setFormat_sql(sessionFactoryElements.selectSingleNode("property[@name='format_sql']").getText());
				hibernateCfg.setHbm2ddl_auto(sessionFactoryElements.selectSingleNode("property[@name='hbm2ddl.auto']").getText());
				hibernateCfg.setDialect(sessionFactoryElements.selectSingleNode("property[@name='hibernate.dialect']").getText());
				hibernateCfg.setAutocommit(
						sessionFactoryElements.selectSingleNode("property[@name='hibernate.connection.autocommit']").getText());
				// mappings
				Element mappingsNode = ((Element) sessionFactoryElements.selectSingleNode("property[@name='annotatedClasses']")).element("list");
				if (mappingsNode!=null) {
					List<Element> mappingsElements=mappingsNode.elements();
					Map<String, Mapping> mappings = new HashMap<String, Mapping>();
					for (Element element : mappingsElements) {
						String resource = element.getTextTrim();
						mappings.put(resource, null);
						buildMappers(mappings
								,resource);
					}
					hibernateCfg.setMappings(mappings);
				}
			}
			return this;
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static void buildMappers(Map<String, Mapping> mappings, String resource) {
		try {
			Class<?> entity=Class.forName(resource);
			Mapping mapper = new Mapping();
			Entity entityAnnotation = entity.getAnnotation(Entity.class);
			if(entityAnnotation!=null) {
				mapper.setMapping_package(entity.getPackage().getName());
				mapper.setMapping_clazz(entity.getSimpleName());
				Table tableAnnotation = entity.getAnnotation(Table.class);
				if (tableAnnotation != null) {
					mapper.setTable(tableAnnotation.name());
				}
				Field[] fs=entity.getDeclaredFields();
				LinkedList<ClassProperty> commonProperty = new LinkedList<ClassProperty>();
				for (Field field : fs) {
					Id idAnnotation = field.getAnnotation(Id.class);
					if(idAnnotation!=null) {
						ClassProperty id=new ClassProperty(field.getName(), idAnnotation.name(), idAnnotation.db_type());
						id.setGenerator(idAnnotation.generator());
						mapper.setId(id);
					}else {
						Column columnAnnotation = field.getAnnotation(Column.class);
						if(columnAnnotation!=null) {
							commonProperty.add(
									new ClassProperty(field.getName(), columnAnnotation.name(), columnAnnotation.db_type()));
						}
					}
				}
				mapper.setCommonProperty(commonProperty);
			}
			mappings.put(mapper.getMapping_package() + "." + mapper.getMapping_clazz(), mapper);
			hibernateCfg.setMappings(mappings);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}

	public AnnoSessionFactory buildSessionFactory() {
		return new AnnoSessionFactory(hibernateCfg);
	}

}
