package org.gcube.common.homelibrary.jcr.workspace.usermanager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.gcube.common.homelibrary.home.HomeLibrary;
import org.gcube.common.homelibrary.home.HomeManagerFactory;
import org.gcube.common.homelibrary.home.exceptions.HomeNotFoundException;
import org.gcube.common.homelibrary.home.exceptions.InternalErrorException;
import org.gcube.common.homelibrary.home.exceptions.UserNotFoundException;
import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.gcube.common.homelibrary.home.workspace.WorkspaceSharedFolder;
import org.gcube.common.homelibrary.home.workspace.accessmanager.ACLType;
import org.gcube.common.homelibrary.home.workspace.exceptions.InsufficientPrivilegesException;
import org.gcube.common.homelibrary.home.workspace.exceptions.ItemNotFoundException;
import org.gcube.common.homelibrary.home.workspace.exceptions.WorkspaceFolderNotFoundException;
import org.gcube.common.homelibrary.home.workspace.exceptions.WrongDestinationException;
import org.gcube.common.homelibrary.home.workspace.usermanager.GCubeGroup;
import org.gcube.common.homelibrary.home.workspace.usermanager.UserManager;
import org.gcube.common.homelibrary.jcr.repository.JCRRepository;
import org.gcube.common.homelibrary.jcr.workspace.JCRWorkspaceSharedFolder;
import org.gcube.common.homelibrary.jcr.workspace.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

public class JCRUserManager implements UserManager{

	private Logger logger = LoggerFactory.getLogger(JCRUserManager.class);
	//	public static final String OWNER 						= "hl:owner";
	public static final String PORTAL_LOGIN  				= "hl:portalLogin";

	protected static final String ACCOUNTING				= "hl:accounting";
	protected static final String NT_ACCOUNTING				= "nthl:accountingSet";

	private static final String SPECIAL_FOLDER_PATH 		= "/Workspace/MySpecialFolders/";


	public static String url;

	public JCRUserManager(){
		super();
		url = JCRRepository.url;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<GCubeGroup> getGroups() throws InternalErrorException {

		List<GCubeGroup> groupsList = null;
		List<String> list = null;
		GetMethod getMethod = null;
		try {
			groupsList =  new ArrayList<GCubeGroup>();
			HttpClient httpClient = new HttpClient();            

			logger.info(url);
			getMethod =  new GetMethod(url + "/ListGroupsServlet?" + JCRRepository.getCredentials());
			httpClient.executeMethod(getMethod);

			logger.trace("Callin List Groups Servlet");
			XStream xstream = new XStream();
			list= (List<String>) xstream.fromXML(getMethod.getResponseBodyAsString());

			for(String group: list){
				//				logger.trace("* " + group);
				groupsList.add(new JCRGroup(group));
			}

		} catch (Exception e) {
			logger.error("Error retrieving Users in UserManager", e);
			throw new InternalErrorException(e);
		} finally {
			if(getMethod != null)
				getMethod.releaseConnection();
		}

		return groupsList;
	}
	
	
	@Override
	public Boolean isGroup(String groupId) throws InternalErrorException {

		Boolean found = false;
		GetMethod getMethod = null;
		try {

			HttpClient httpClient = new HttpClient();            

			getMethod =  new GetMethod(url + "/IsGroupServlet?" + JCRRepository.getCredentials() + "&groupName=" + groupId );
			httpClient.executeMethod(getMethod);

			logger.trace("Is " +  groupId + " a group? " + getMethod.getResponseBodyAsString());

			XStream xstream = new XStream();
			found = (Boolean) xstream.fromXML(getMethod.getResponseBodyAsString());


		} catch (Exception e) {
			logger.error("Error retrieving Users in UserManager", e);
			throw new InternalErrorException(e);
		} finally {
			if(getMethod != null)
				getMethod.releaseConnection();
		}

		//get return
		return found;	
	}
	

	@Override
	public GCubeGroup getGroup(String groupId) throws InternalErrorException {

		JCRGroup group = null;

		try {
			if (isGroup(groupId))
				group = new JCRGroup(groupId);

		} catch (Exception e) {
			logger.error("Error retrieving Users in UserManager", e);
			throw new InternalErrorException(e);
		} 

		//get return
		return group;	
	}

	@Override
	public GCubeGroup createGroup(String scope) throws InternalErrorException {
		logger.trace("Create a new group: "+ scope);

		String newGroupName = Utils.getGroupByScope(scope);
		String displayName = getDisplayName(scope);

		GetMethod getMethod = null;
		GCubeGroup group = null;
		try {

			HttpClient httpClient = new HttpClient();            

			getMethod =  new GetMethod(url + "/CreateGroupServlet?" + JCRRepository.getCredentials() + "&groupName=" + newGroupName+ "&displayName=" + displayName);
			httpClient.executeMethod(getMethod);
			logger.trace("Create Group " + newGroupName);
			logger.trace("Response " + getMethod.getResponseBodyAsString());

			try{
				group = new JCRGroup(newGroupName);			
			}catch (Exception e) {
				logger.error("Error creating a new group " , e);
			}



		} catch (Exception e) {
			logger.error("Error retrieving Users in UserManager", e);
		} finally {
			if(getMethod != null)
				getMethod.releaseConnection();
		}

		return group;
	}


	private String getDisplayName(String groupName) {
		String[] groupNameSplit = groupName.split("/");
		int size = groupNameSplit.length;
		String displayName = groupNameSplit[size-1];
		return displayName;
	}


	@Override
	public boolean deleteAuthorizable(String groupName) throws InternalErrorException {
		GetMethod getMethod = null;
		try {

			HttpClient httpClient = new HttpClient();            

			getMethod =  new GetMethod(url + "/DeleteAuthorizableServlet?" + JCRRepository.getCredentials() + "&groupName=" + groupName );
			httpClient.executeMethod(getMethod);
			logger.trace("Delete user or group " + groupName);
			logger.trace("Response " + getMethod.getResponseBodyAsString());

		} catch (Exception e) {
			logger.error("Error removing User or group in UserManager", e);
			return false;
		} finally {
			if(getMethod != null)
				getMethod.releaseConnection();
		}
		return true;

	}


	@SuppressWarnings("unchecked")
	@Override
	public List<String> getUsers() throws InternalErrorException {
		List<String> users = null;
		GetMethod getMethod = null;
		try {

			HttpClient httpClient = new HttpClient();            

			getMethod =  new GetMethod(url + "/ListUsersServlet?" + JCRRepository.getCredentials());
			httpClient.executeMethod(getMethod);

			XStream xstream = new XStream();
			users= (List<String>) xstream.fromXML(getMethod.getResponseBodyAsString());

		} catch (Exception e) {
			logger.error("Error retrieving Users in UserManager", e);
			throw new InternalErrorException(e);
		} finally {
			if(getMethod != null)
				getMethod.releaseConnection();
		}
		return users;
	}




	@Override
	public boolean createUser(String name, String version) throws InternalErrorException {
		Boolean created = false;
		GetMethod getMethod = null;
		try {

			HttpClient httpClient = new HttpClient();            
			//			System.out.println(url);

			String pass = getSecurePassword(name);
			//			System.out.println(pass);
			getMethod =  new GetMethod(url + "/CreateUserServlet?" + JCRRepository.getCredentials() + "&userName=" + name +"&pwd="+ pass);
			httpClient.executeMethod(getMethod);

			XStream xstream = new XStream();
			created = (Boolean) xstream.fromXML(getMethod.getResponseBodyAsString());
			logger.trace("Create User " + name );
			logger.trace("Response " + created);

		} catch (Exception e) {
			logger.error("Error retrieving Users in UserManager", e);
			return false;
		} finally {
			if(getMethod != null)
				getMethod.releaseConnection();
		}

		return created;
	}

	@Override
	public boolean createUser(String name, String pass, String version) throws InternalErrorException {
		Boolean created = false;
		GetMethod getMethod = null;
		try {

			HttpClient httpClient = new HttpClient();            
			getMethod =  new GetMethod(url + "/CreateUserServlet?" + JCRRepository.getCredentials() + "&userName=" + name +"&pwd="+ pass);
			httpClient.executeMethod(getMethod);

			XStream xstream = new XStream();
			created = (Boolean) xstream.fromXML(getMethod.getResponseBodyAsString());
			logger.trace("Create User " + name );
			logger.trace("Response " + created);

		} catch (Exception e) {
			logger.error(name + " already exists", e);
			return false;
		} finally {
			if(getMethod != null)
				getMethod.releaseConnection();
		}

		return created;
	}

	//create a password
	public static String getSecurePassword(String message) throws InternalErrorException {
		String digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(message.getBytes("UTF-8"));

			//converting byte array to Hexadecimal String
			StringBuilder sb = new StringBuilder(2*hash.length);
			for(byte b : hash){
				sb.append(String.format("%02x", b&0xff));
			}
			digest = sb.toString();

		} catch (UnsupportedEncodingException e) {
			throw new InternalErrorException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new InternalErrorException(e);
		}
		return digest;
	}


	@Override
	public boolean associateUserToGroup(String scope, String username, String portalLogin)
			throws InternalErrorException, ItemNotFoundException {

		HomeManagerFactory factory = HomeLibrary
				.getHomeManagerFactory();
		UserManager gm = factory.getUserManager();

		String VREFolder = Utils.getGroupByScope(scope);
		String groupId = VREFolder;
		String manager = getManager(VREFolder);
		try{
			if (gm.createUser(username, null))
				logger.trace(username + " has been created");

			//add user to group
			JCRGroup group = (JCRGroup) gm.getGroup(groupId);
			if (group!=null){
				group.addMember(username);								
			}

			//add user to share
			Workspace ws = null;
			try {
				ws = factory.getHomeManager()
						.getHome(manager)
						.getWorkspace();
			} catch (WorkspaceFolderNotFoundException e) {
				throw new InternalErrorException(e);
			} catch (HomeNotFoundException e) {
				throw new InternalErrorException(e);
			} catch (UserNotFoundException e) {
				throw new InternalErrorException(e);
			}

			if (ws!=null){
				JCRWorkspaceSharedFolder folder = (JCRWorkspaceSharedFolder) ws.getItemByPath(SPECIAL_FOLDER_PATH + VREFolder );
				logger.trace("VRE folder path: " + folder.getPath());

				//share folder with the new member
				ArrayList<String> user = new ArrayList<String>();
				user.add(username);

				try {
					folder.share(user);
				} catch (InsufficientPrivilegesException
						| WrongDestinationException e) {
					throw new InternalErrorException(e);
				}
			}

		} catch (InternalErrorException e) {
			throw new RuntimeException(e);
		}


		return true;
	}



	@Override
	public boolean removeUserFromGroup(String scope, String userToRemove,
			String portalLogin) throws InternalErrorException , ItemNotFoundException {
		UserManager gm = HomeLibrary
				.getHomeManagerFactory().getUserManager();

		String VREFolder = Utils.getGroupByScope(scope);
		String groupId = VREFolder;
		String manager = getManager(VREFolder);	

		try{
			//remove the user from the VRE group
			GCubeGroup group = gm.getGroup(groupId);
			if (group!=null)
				group.removeMember(userToRemove);

			//unshare the VRE folder
			Workspace ws = null;
			try {
				ws = HomeLibrary
						.getHomeManagerFactory()
						.getHomeManager()
						.getHome(manager)
						.getWorkspace();

			} catch (WorkspaceFolderNotFoundException e) {
				throw new InternalErrorException(e);
			} catch (HomeNotFoundException e) {
				throw new InternalErrorException(e);
			} catch (UserNotFoundException e) {
				throw new InternalErrorException(e);
			}

			if (ws!=null){

				//				System.out.println("ws.getRoot().getPath() " + ws.getRoot().getPath());
				JCRWorkspaceSharedFolder folder = (JCRWorkspaceSharedFolder) ws.getItemByPath(SPECIAL_FOLDER_PATH + VREFolder);
				logger.trace("VRE folder path: " + folder.getPath());
				folder.unShare(userToRemove);		
			}

		}catch (Exception e) {
			return false;
		}
		return true;
	}



	@Override
	public boolean setAdministrator(String scope, String username,
			String portalLogin) throws InternalErrorException,
			ItemNotFoundException {

		String VREFolder = Utils.getGroupByScope(scope);

		Workspace ws = null;
		String manager = getManager(VREFolder);
		try {
			ws = HomeLibrary
					.getHomeManagerFactory()
					.getHomeManager()
					.getHome(manager)
					.getWorkspace();

			WorkspaceSharedFolder folder = (WorkspaceSharedFolder) ws.getItemByPath(SPECIAL_FOLDER_PATH + VREFolder);

			List<String> administator = new ArrayList<String>();
			administator.add(username);
			folder.setACL(administator, ACLType.ADMINISTRATOR);

		}catch (Exception e) {
			return false;
		}
		return true;
	}


	@Override
	public boolean removeAdministrator(String scope, String username,
			String portalLogin) throws InternalErrorException,
			ItemNotFoundException {

		String VREFolder = Utils.getGroupByScope(scope);

		Workspace ws = null;
		String manager = getManager(VREFolder);
		try {
			ws = HomeLibrary
					.getHomeManagerFactory()
					.getHomeManager()
					.getHome(manager)
					.getWorkspace();

			WorkspaceSharedFolder folder = (WorkspaceSharedFolder) ws.getItemByPath(SPECIAL_FOLDER_PATH + VREFolder);

			List<String> administator = new ArrayList<String>();
			administator.add(username);
			folder.deleteACL(administator);


		}catch (Exception e) {
			return false;
		}
		return true;
	}



	/**
	 * Get manager name
	 * @param scope
	 * @return manager
	 */
	private String getManager(String scope) {
		String manager = scope + "-Manager";
		return manager;
	}



	@Override
	public String getVersionByUser(String user) throws InternalErrorException {
		String displayName;
		GetMethod getMethod = null;
		try {

			HttpClient httpClient = new HttpClient();      
//			System.out.println(url + "/GetVersionServlet?" + JCRRepository.getCredetials() + "&user=" + user);
			getMethod =  new GetMethod(url + "/GetVersionServlet?" + JCRRepository.getCredentials() + "&user=" + user);
			httpClient.executeMethod(getMethod);

			logger.debug("Response " + getMethod.getResponseBodyAsString());


			XStream xstream = new XStream();
			displayName = (String) xstream.fromXML(getMethod.getResponseBodyAsString());


		} catch (Exception e) {
			return null;
		} finally {
			if(getMethod != null)
				getMethod.releaseConnection();
		}

		//get return
		return displayName;	
	}


	@Override
	public boolean setVersionByUser(String user, String version)
			throws InternalErrorException {
		boolean flag;
		GetMethod getMethod = null;
		try {

			HttpClient httpClient = new HttpClient();            
			getMethod =  new GetMethod(url + "/SetVersionServlet?" + JCRRepository.getCredentials() + "&user=" + user + "&version=" + version);
			httpClient.executeMethod(getMethod);

			logger.debug("Response " + getMethod.getResponseBodyAsString());

			flag = true;
			//			XStream xstream = new XStream();
			//			flag = (boolean) xstream.fromXML(getMethod.getResponseBodyAsString());


		} catch (Exception e) {
			flag = false;
			//			logger.error("Error retrieving Users in UserManager", e);
			//			throw new InternalErrorException(e);
		} finally {
			if(getMethod != null)
				getMethod.releaseConnection();
		}

		//get return
		return flag;	
	}



}
