/**
 * 
 */
package org.gcube.accounting.persistence;

import java.security.Key;
import java.util.List;
import java.util.Map;

import org.gcube.common.encryption.StringEncrypter;
import org.gcube.common.resources.gcore.ServiceEndpoint;
import org.gcube.common.resources.gcore.ServiceEndpoint.AccessPoint;
import org.gcube.common.resources.gcore.ServiceEndpoint.Property;
import org.gcube.common.resources.gcore.utils.Group;
import org.gcube.common.scope.api.ScopeProvider;
import org.gcube.documentstore.persistence.PersistenceBackend;
import org.gcube.documentstore.persistence.PersistenceBackendConfiguration;
import org.gcube.resources.discovery.client.api.DiscoveryClient;
import org.gcube.resources.discovery.client.queries.api.SimpleQuery;
import org.gcube.resources.discovery.icclient.ICFactory;

/**
 * @author Luca Frosini (ISTI - CNR) http://www.lucafrosini.com/
 */
public class AccountingPersistenceConfiguration extends PersistenceBackendConfiguration {
	
	protected static final String TARGET_SCOPE = "targetScope";

	protected static final String SERVICE_ENDPOINT_CATEGORY = "Accounting";
	protected static final String SERVICE_ENDPOINT_NAME = "Persistence";
	
	public static final String URL_PROPERTY_KEY = "URL";
	public static final String USERNAME_PROPERTY_KEY = "username";
	public static final String PASSWORD_PROPERTY_KEY = "password";
	
	public AccountingPersistenceConfiguration(){
		super();
	}
	
	public AccountingPersistenceConfiguration(Class<? extends PersistenceBackend> clz) throws Exception {
		super(clz);
		ServiceEndpoint serviceEndpoint = getServiceEndpoint(SERVICE_ENDPOINT_CATEGORY, SERVICE_ENDPOINT_NAME, clz);
		setValues(serviceEndpoint, clz);
	}
	
	protected ServiceEndpoint getServiceEndpoint(String serviceEndpointCategory, String serviceEndpointName, Class<? extends PersistenceBackend> clz){
		SimpleQuery query = ICFactory.queryFor(ServiceEndpoint.class);
		query.addCondition(String.format("$resource/Profile/Category/text() eq '%s'", serviceEndpointCategory));
		query.addCondition(String.format("$resource/Profile/Name/text() eq '%s'", serviceEndpointName));
		query.addCondition(String.format("$resource/Profile/AccessPoint/Interface/Endpoint/@EntryName eq '%s'", clz.getSimpleName()));
		/*
		 * Used in old version
		 * query.addCondition(String.format("$resource/Profile/AccessPoint/Properties/Property/Name/text() eq '%s'", PERSISTENCE_CLASS_NAME));
		 * query.addCondition(String.format("$resource/Profile/AccessPoint/Properties/Property/Value/text() eq '%s'", clz.getSimpleName()));
		 */
		query.setResult("$resource");
		
		DiscoveryClient<ServiceEndpoint> client = ICFactory.clientFor(ServiceEndpoint.class);
		List<ServiceEndpoint> serviceEndpoints = client.submit(query);
		if(serviceEndpoints.size()>1){
			//String scope = BasicUsageRecord.getScopeFromToken();
			String scope = ScopeProvider.instance.get();
			query.addCondition(String.format("$resource/Profile/AccessPoint/Properties/Property/Name/text() eq '%s'", TARGET_SCOPE));
			query.addCondition(String.format("$resource/Profile/AccessPoint/Properties/Property/Value/text() eq '%s'", scope));
			serviceEndpoints = client.submit(query);
		}
		
		return serviceEndpoints.get(0);
	}
	
	private static String decrypt(String encrypted, Key... key) throws Exception {
		return StringEncrypter.getEncrypter().decrypt(encrypted);
	}
	
	protected void setValues(ServiceEndpoint serviceEndpoint, Class<? extends PersistenceBackend> clz) throws Exception{
		Group<AccessPoint> accessPoints = serviceEndpoint.profile().accessPoints();
		for(AccessPoint accessPoint : accessPoints){
			if(accessPoint.name().compareTo(clz.getSimpleName())==0){

				addProperty(URL_PROPERTY_KEY, accessPoint.address());
				addProperty(USERNAME_PROPERTY_KEY, accessPoint.username());
				
				String encryptedPassword = accessPoint.password();
				String password = decrypt(encryptedPassword);
				addProperty(PASSWORD_PROPERTY_KEY, password);
				
				Map<String, Property> propertyMap = accessPoint.propertyMap();
				for(String key : propertyMap.keySet()){
					Property property = propertyMap.get(key);
					String value = property.value();
					if(property.isEncrypted()){
						value = decrypt(value);
					}
					addProperty(key, value);
				}
				
			}
		}
	}
		
}
