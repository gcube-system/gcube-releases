package org.gcube.data.analysis.statisticalmanager.persistence;

import java.util.concurrent.ConcurrentHashMap;

import org.gcube.common.scope.api.ScopeProvider;
import org.gcube.data.analysis.statisticalmanager.exception.StatisticalManagerException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateManager {
	
	private static Logger logger = LoggerFactory.getLogger(HibernateManager.class);
	
	private static final String DRIVER_CLASS = "hibernate.connection.driver_class";
	private static final String URL = "hibernate.connection.url";
	private static final String USERNAME = "hibernate.connection.username";
	private static final String PASSWORD = "hibernate.connection.password";
	

	private static final ConcurrentHashMap<String, HibernateManager> managerMap=new ConcurrentHashMap<>();
	
	public synchronized static HibernateManager get() throws StatisticalManagerException, Exception{
		String currentScope=ScopeProvider.instance.get();
		logger.trace("Getting DB under scope "+currentScope);
		if(!managerMap.containsKey(currentScope)){
			AccessPointDescriptor desc=RuntimeResourceManager.getDatabaseProfile(DatabaseType.HIBERNATE);
			logger.debug("Initializing with "+desc+" under "+currentScope);
			managerMap.put(currentScope, new HibernateManager(desc));
		}
		return managerMap.get(currentScope);
	}
	
	
	private SessionFactory sessionFactory;
	
	private HibernateManager(AccessPointDescriptor desc) {
		try {
		
			
			Configuration configuration = new Configuration().configure(
					HibernateManager.class.getResource("hibernate.cfg.xml"));
			configuration.setProperty(DRIVER_CLASS, "org.postgresql.Driver");
			configuration.setProperty(URL, desc.getUrl());
			configuration.setProperty(USERNAME, desc.getUsername());
			configuration.setProperty(PASSWORD, desc.getPassword());
			sessionFactory = configuration.buildSessionFactory();
		}
		catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			logger.error("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public SessionFactory getSessionFactory()  {
		
		if (sessionFactory == null)
			logger.error("SessionFactory is not initialized");
		
		return sessionFactory;
	}
	
	public void closeSession(Session session) {
		try {if(session!=null)session.close();} catch(Exception e){};
	}
	
	public void roolbackTransaction(Transaction t){
		try {if(t!=null)t.rollback();} catch(Exception e){};
	}

}
