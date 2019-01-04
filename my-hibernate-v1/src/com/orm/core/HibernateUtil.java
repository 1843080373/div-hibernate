package com.orm.core;

public class HibernateUtil {
	private static final AnnoSessionFactory ANNO_SESSION_FACTORY;
	private static final XMLSessionFactory XML_SESSION_FACTORY;
    static {
        try {
        	ANNO_SESSION_FACTORY = new AnnoConfiguration().configure().buildSessionFactory();
        	XML_SESSION_FACTORY=new XMLConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
        System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
	public static AnnoSessionFactory getAnnoSessionFactory() {
		return ANNO_SESSION_FACTORY;
	}
	
	public static XMLSessionFactory getXMLSessionFactory() {
		return XML_SESSION_FACTORY;
	}
}
