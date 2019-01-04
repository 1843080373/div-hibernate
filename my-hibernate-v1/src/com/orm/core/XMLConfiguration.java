package com.orm.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import com.orm.utils.StringUtils;
@SuppressWarnings("unchecked")
public class XMLConfiguration {

	private static HibernateCfg hibernateCfg;

	public XMLConfiguration configure() {
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
				List<Element> mappingsElements = sessionFactoryElements.elements("mapping");
				if (!mappingsElements.isEmpty()) {
					Map<String, Mapping> mappings = new HashMap<String, Mapping>();
					for (Element element : mappingsElements) {
						String resource = element.attributeValue("resource");
						mappings.put(StringUtils.str2Package(resource), null);
						buildMappers(mappings
								,new FileInputStream(new File("src/"+resource)));
					}
					hibernateCfg.setMappings(mappings);
				}
			}
			return this;
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
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

	private static void buildMappers(Map<String, Mapping> mappings, InputStream inputStream) {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			Mapping mapper = new Mapping();
			// hibernateMapping
			Element hibernateMappingElements = document.getRootElement();
			if (hibernateMappingElements != null) {
				mapper.setMapping_package(hibernateMappingElements.attributeValue("package"));
				Element classElements = hibernateMappingElements.element("class");
				if (classElements != null) {
					mapper.setTable(classElements.attributeValue("table"));
					mapper.setMapping_clazz(classElements.attributeValue("name"));
				}
				Element idElements = classElements.element("id");
				if (idElements != null) {
					ClassProperty id=new ClassProperty(idElements.attributeValue("name"), idElements.attributeValue("column"), idElements.attributeValue("db-type"));
					id.setGenerator(idElements.element("generator").attributeValue("class"));
					mapper.setId(id);
				}
				List<Element> propertyElements = classElements.elements("property");
				if (!propertyElements.isEmpty()) {
					LinkedList<ClassProperty> commonProperty = new LinkedList<ClassProperty>();
					for (Element element : propertyElements) {
						commonProperty.add(
								new ClassProperty(element.attributeValue("name"), element.attributeValue("column"), element.attributeValue("db-type")));
					}
					mapper.setCommonProperty(commonProperty);
				}
			}
			mappings.put(mapper.getMapping_package() + "." + mapper.getMapping_clazz(), mapper);
			hibernateCfg.setMappings(mappings);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public XMLSessionFactory buildSessionFactory() {
		return new XMLSessionFactory(hibernateCfg);
	}

}
