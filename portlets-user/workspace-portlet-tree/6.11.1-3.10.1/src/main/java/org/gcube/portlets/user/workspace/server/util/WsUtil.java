/**
 * 
 */
package org.gcube.portlets.user.workspace.server.util;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.gcube.application.framework.core.session.ASLSession;
import org.gcube.application.framework.core.session.SessionManager;
import org.gcube.applicationsupportlayer.social.ApplicationNotificationsManager;
import org.gcube.applicationsupportlayer.social.NotificationsManager;
import org.gcube.common.homelibrary.home.HomeLibrary;
import org.gcube.common.homelibrary.home.exceptions.HomeNotFoundException;
import org.gcube.common.homelibrary.home.exceptions.InternalErrorException;
import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.gcube.common.homelibrary.home.workspace.exceptions.WorkspaceFolderNotFoundException;
import org.gcube.common.scope.api.ScopeProvider;
import org.gcube.portal.custom.scopemanager.scopehelper.ScopeHelper;
import org.gcube.portlets.user.workspace.client.model.InfoContactModel;
import org.gcube.portlets.user.workspace.server.GWTWorkspaceBuilder;
import org.gcube.portlets.user.workspace.server.notifications.NotificationsProducer;
import org.gcube.portlets.user.workspace.server.resolver.UriResolverReaderParameterForResolverIndex;
import org.gcube.portlets.user.workspace.server.resolver.UriResolverReaderParameterForResolverIndex.RESOLVER_TYPE;
import org.gcube.portlets.user.workspace.server.shortener.UrlShortener;
import org.gcube.portlets.user.workspace.server.util.resource.PropertySpecialFolderReader;
import org.gcube.portlets.user.workspace.server.util.scope.ScopeUtilFilter;

import com.liferay.portal.service.UserLocalServiceUtil;


/**
 * 
 * @author Francesco Mangiacrapa francesco.mangiacrapa@isti.cnr.it
 * @Feb 18, 2014
 *
 */
public class WsUtil {

	public static final String USERNAME_ATTRIBUTE = ScopeHelper.USERNAME_ATTRIBUTE;
	public static final String FOLDERIMPORTER_ATTRIBUTE = "FOLDER_IMPORTER";
	public static final String METADATACONVERTER_ATTRIBUTE = "METADATA_CONVERTER";
	public static final String WORKSPACE_EVENT_COLLECTOR_ATTRIBUTE = "EVENT_COLLECTOR";
	public static final String WORKSPACEBUILDER_ATTRIBUTE = "WORKSPACEBUILDER";
	public static final String NOTIFICATION_MANAGER = "NOTIFICATION_MANAGER";
	public static final String NOTIFICATION_PRODUCER = "NOTIFICATION_PRODUCER";
	public static final String WS_RUN_IN_TEST_MODE = "WS_RUN_IN_TEST_MODE";
	public static final String WORKSPACE_SCOPE_UTIL = "WORKSPACE_SCOPE_UTIL";
	public static final String URL_SHORTENER_SERVICE = "URL_SHORTENER_SERVICE";
	public static final String URI_RESOLVER_SERVICE = "URI_RESOLVER_SERVICE";
	public static final String PROPERTY_SPECIAL_FOLDER = "PROPERTY_SPECIAL_FOLDER";
	public static final String NOTIFICATION_PORTLET_CLASS_ID = "org.gcube.portlets.user.workspace.server.GWTWorkspaceServiceImpl"; //IN DEV
	
	public static final String TEST_SCOPE = "/gcube/devsec";
	
//	public static final String TEST_USER = "pasquale.pagano";
//	public static final String TEST_USER = "federico.defaveri";
//	public static final String TEST_USER = "massimiliano.assante";
//	public static final String TEST_USER = "pasquale.pagano";
//	public static final String TEST_USER = "aureliano.gentile";
//	public static final String TEST_USER = "antonio.gioia";
	
	//COMMENT THIS FOR RELEASE
//	public static final String TEST_USER = "francesco.mangiacrapa";
//	public static final String TEST_USER_FULL_NAME = "Francesco Mangiacrapa";
	
	//UNCOMMENT THIS FOR RELEASE
	public static String TEST_USER = "test.user";
	public static String TEST_USER_FULL_NAME = "Test User";
	
	
	protected static Logger logger = Logger.getLogger(WsUtil.class);

//	public static boolean withoutPortal = false;
	
	/**
	 * 
	 * @return true if you're running into the portal, false if in development
	 */
	public static boolean isWithinPortal() {
		try {
			UserLocalServiceUtil.getService();
			return true;
		} 
		catch (Exception ex) {			
			logger.trace("Development Mode ON");
			return false;
		}			
	}
	
	public static ASLSession getAslSession(HttpSession httpSession)
	{
		String sessionID = httpSession.getId();
		String user = (String) httpSession.getAttribute(USERNAME_ATTRIBUTE);
		ASLSession session;
		
		if (user == null) {
			
			/*USE ANOTHER ACCOUNT (OTHERWHISE BY TEST_USER) FOR RUNNING
			 * COMMENT THIS IN DEVELOP ENVIROMENT (UNCOMMENT IN PRODUCTION)*/
			user=TEST_USER;
			
			if (!isWithinPortal()) { //DEV MODE
				user = "francesco.mangiacrapa";
				TEST_USER_FULL_NAME = "Francesco Mangiacrapa";
			}
			
			logger.warn("WORKSPACE PORTLET STARTING IN TEST MODE - NO USER FOUND - PORTLETS STARTING WITH FOLLOWING SETTINGS:");
			logger.warn("session id: "+sessionID);
			logger.warn("TEST_USER: "+user);
			logger.warn("TEST_SCOPE: "+TEST_SCOPE);
			logger.warn("USERNAME_ATTRIBUTE: "+USERNAME_ATTRIBUTE);
			session = SessionManager.getInstance().getASLSession(sessionID, user);
			session.setScope(TEST_SCOPE);
			
	
			//MANDATORY FOR SOCIAL LIBRARY
			session.setUserAvatarId(user + "Avatar");
			session.setUserFullName(TEST_USER_FULL_NAME);
			session.setUserEmailAddress(user + "@mail.test");
			
			//SET HTTP SESSION ATTRIBUTE
			httpSession.setAttribute(USERNAME_ATTRIBUTE, user);
			
//			withoutPortal = true;
			
			return session;
			
		}
		
		return SessionManager.getInstance().getASLSession(sessionID, user);
	}
	
	/**
	 * 
	 * @param httpSession
	 * @return true if current username into ASL session is WsUtil.TEST_USER, false otherwise
	 * @throws Exception
	 */
	public static boolean isSessionExpired(HttpSession httpSession) throws Exception {
		logger.trace("workspace session validating...");
		//READING USERNAME FROM ASL SESSION
		String userUsername = getAslSession(httpSession).getUsername();
		boolean isTestUser = userUsername.compareTo(WsUtil.TEST_USER)==0;
		
		//TODO UNCOMMENT THIS FOR RELEASE
		logger.trace("Is "+WsUtil.TEST_USER+" test user? "+isTestUser);
		
		if(isTestUser){
			logger.error("workspace session is expired! username is: "+WsUtil.TEST_USER);
			return true; //is TEST_USER, session is expired
		}

		logger.trace("workspace session is valid! current username is: "+userUsername);
		
		return false;
		
	}
	

	public static Workspace getWorkspace(final HttpSession httpSession) throws InternalErrorException, HomeNotFoundException, WorkspaceFolderNotFoundException
	{
		
		logger.trace("Get Workspace");
		final ASLSession session = getAslSession(httpSession);
		logger.trace("ASLSession scope: "+session.getScope() + " username: "+session.getUsername());

		ScopeProvider.instance.set(session.getScope());
		logger.trace("Scope provider instancied");
		
		Workspace workspace = HomeLibrary.getUserWorkspace(session.getUsername());

		/*
		if (session.getAttribute(METADATACONVERTER_ATTRIBUTE) == null){

			logger.trace("Initializing the Metadata converter");

			Thread mcLoader = new Thread(new Runnable(){

				public void run() {
					try {
						MetadataConverter mc = new MetadataConverter(logger);
						mc.setup(session);
						session.setAttribute(METADATACONVERTER_ATTRIBUTE, mc);
					}catch (Exception e) {
						logger.error("Error initializing the Metadata Converter: "+e.getMessage());
					}
				}

			});

			mcLoader.start();
		}
		if (session.getAttribute(WORKSPACE_EVENT_COLLECTOR_ATTRIBUTE) == null){
			

			logger.trace("Initializing the event collector");

			//we prepare the event collector
			WorkspaceEventCollector eventCollector = new WorkspaceEventCollector();
			workspace.addWorkspaceListener(eventCollector);
			session.setAttribute(WORKSPACE_EVENT_COLLECTOR_ATTRIBUTE, eventCollector);
		}
		*/
		
		if (session.getAttribute(WORKSPACEBUILDER_ATTRIBUTE) == null)
		{
			logger.trace("Initializing the workspace area builder");
			
			GWTWorkspaceBuilder builder = new GWTWorkspaceBuilder();
			
			//ADDED 03/09/2013
			builder.setUserLogged(new InfoContactModel(session.getUsername(), session.getUsername(), session.getUserFullName(), false));
			
			session.setAttribute(WORKSPACEBUILDER_ATTRIBUTE, builder);
		}

		return workspace;

	}


//	public static MetadataConverter getMetadataConverter(Logger logger, HttpSession httpSession)
//	{
//		MetadataConverter mc = (MetadataConverter) httpSession.getAttribute(METADATACONVERTER_ATTRIBUTE);
//
//		if (mc==null)
//		{
//
//			mc = new MetadataConverter(logger);
//			ASLSession session = getAslSession(httpSession);
//			mc.setup(session);
//			session.setAttribute(METADATACONVERTER_ATTRIBUTE, mc);
//		}
//
//		mc.isReady();
//
//		return mc;
//	}

//	public static WorkspaceEventCollector getEventCollector(HttpSession httpSession)
//	{
//		ASLSession session = getAslSession(httpSession);
//		return (WorkspaceEventCollector) session.getAttribute(WsUtil.WORKSPACE_EVENT_COLLECTOR_ATTRIBUTE);
//	}

	public static GWTWorkspaceBuilder getGWTWorkspaceBuilder(HttpSession httpSession)
	{
		ASLSession session = getAslSession(httpSession);
		return (GWTWorkspaceBuilder) session.getAttribute(WsUtil.WORKSPACEBUILDER_ATTRIBUTE);
	}
	
	public static NotificationsManager getNotificationManager(ASLSession session)
	{
		
		NotificationsManager notifMng = (NotificationsManager) session.getAttribute(NOTIFICATION_MANAGER);
		
		if (notifMng == null) {
			try{
				logger.trace("Create new NotificationsManager for user: "+session.getUsername());
				logger.trace("New ApplicationNotificationsManager with portlet class name: "+NOTIFICATION_PORTLET_CLASS_ID);
				notifMng = new ApplicationNotificationsManager(session, NOTIFICATION_PORTLET_CLASS_ID);
				session.setAttribute(NOTIFICATION_MANAGER, notifMng);
			}catch (Exception e) {
				logger.error("An error occurred instancing ApplicationNotificationsManager for user: "+session.getUsername(),e);
			}
		}
		
		return notifMng;
	}
	
	public static NotificationsProducer getNotificationProducer(ASLSession session)
	{
		
		NotificationsProducer notifProducer = (NotificationsProducer) session.getAttribute(NOTIFICATION_PRODUCER);
		
		if (notifProducer == null) {
			logger.trace("Create new Notification Producer for user: "+session.getUsername());
			notifProducer = new NotificationsProducer(session);
			session.setAttribute(NOTIFICATION_PRODUCER, notifProducer);
		}
		
		return notifProducer;
	}

	public static String getUserId(HttpSession httpSession) {
		
		ASLSession session = getAslSession(httpSession);
		
		return session.getUsername();
	}
	
	public static boolean isVRE(ASLSession session){
		
		String currentScope = session.getScopeName();
		
		int slashCount = StringUtils.countMatches(currentScope, "/");
		
		if(slashCount < 3){
			logger.trace("currentScope is not VRE");
			return false;
		}
		
		logger.trace("currentScope is VRE");
		return true;
		
	}
	
	public static ScopeUtilFilter getScopeUtilFilter(HttpSession httpSession){
		
		ASLSession session = getAslSession(httpSession);
		ScopeUtilFilter scopeUtil = null;
		try{
			scopeUtil = (ScopeUtilFilter) session.getAttribute(WsUtil.WORKSPACE_SCOPE_UTIL);
			
			if(scopeUtil==null){
				scopeUtil = new ScopeUtilFilter(session.getScopeName(),true);
				
			}
		}catch (Exception e) {
			logger.error("an error occurred in getscope filter "+e);
		}
		
		return scopeUtil;
	}


	/**
	 * @param session
	 * @return
	 */
	public static UrlShortener getUrlShortener(HttpSession httpSession) {
		
		ASLSession session = getAslSession(httpSession);
		UrlShortener shortener = null;
		try{
			shortener = (UrlShortener) session.getAttribute(WsUtil.URL_SHORTENER_SERVICE);
			
			if(shortener==null){
				shortener = new UrlShortener(session.getScope().toString());
				session.setAttribute(URL_SHORTENER_SERVICE, shortener);
			}
			
		}catch (Exception e) {
			logger.error("an error occurred in instancing url shortener ",e);
		}

		return shortener;
	}


	/**
	 * @param session
	 * @return
	 */
	public static UriResolverReaderParameterForResolverIndex getUriResolver(HttpSession httpSession) {
		
		ASLSession session = getAslSession(httpSession);
		
		UriResolverReaderParameterForResolverIndex uriResolver = null;
		try{
			uriResolver = (UriResolverReaderParameterForResolverIndex) session.getAttribute(WsUtil.URI_RESOLVER_SERVICE);
			
			if(uriResolver==null){
				uriResolver = new UriResolverReaderParameterForResolverIndex(session.getScope().toString(),RESOLVER_TYPE.SMP_ID);
				session.setAttribute(URI_RESOLVER_SERVICE, uriResolver);
			}
			
		}catch (Exception e) {
			logger.error("an error occurred instancing URI Resolver ",e);
		}

		return uriResolver;
	}

	/**
	 * @param session
	 * @return
	 */
	public static PropertySpecialFolderReader getPropertySpecialFolderReader(HttpSession httpSession, String pathProperty) {
		ASLSession session = getAslSession(httpSession);
		
		PropertySpecialFolderReader psFolderReader = null;
		try{
			psFolderReader = (PropertySpecialFolderReader) session.getAttribute(WsUtil.PROPERTY_SPECIAL_FOLDER);
			
			if(psFolderReader==null){
				psFolderReader = new PropertySpecialFolderReader(pathProperty);
				session.setAttribute(PROPERTY_SPECIAL_FOLDER, psFolderReader);
			}
			
		}catch (Exception e) {
			logger.error("an error occurred instancing PropertySpecialFolderReader ",e);
		}

		return psFolderReader;
	}

}
