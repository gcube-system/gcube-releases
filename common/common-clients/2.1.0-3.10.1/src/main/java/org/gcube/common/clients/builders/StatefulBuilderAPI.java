package org.gcube.common.clients.builders;

import java.net.URI;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.xml.ws.wsaddressing.W3CEndpointReference;

import org.gcube.common.clients.config.Property;
import org.gcube.common.clients.queries.Query;

/**
 * Defines a DSL for proxy builders of stateful services.
 * 
 * @author Fabio Simeoni
 *
 */
public class StatefulBuilderAPI {

	/**
	 * The first clause of the DSL.
	 * 
	 * @author Fabio Simeoni
	 *
	 * @param<A> the type of service addresses
	 * @param <P> the type of service proxies
	 */
	public static interface Builder<A,P> {
		
		/**
		 * Configures a query for service instances.
		 * @param query the query
		 * @return further configuration options
		 */
		public SecondClause<P> matching(Query<A> query);
		
		/**
		 * Configures the address of a given service instance.
		 * @param address the address
		 * @return further configuration options
		 * @throws IllegalArgumentException if the address is invalid
		 */
		public SecondClause<P> at(W3CEndpointReference address) throws IllegalArgumentException;
		
		/**
		 * Configures the address of a given service instance.
		 * @param the key of the instance
		 * @param host the host of the corresponding service endpoint
		 * @param port the port of the corresponding service endpoint
		 * @return further configuration options
		 * @throws IllegalArgumentException if the address is invalid
		 */
		public SecondClause<P> at(String key, String host, int port) throws IllegalArgumentException;
		
		/**
		 * Configures the address of a given service instance.
		 * @param the key of the instance
		 * @param address the address of the corresponding service endpoint
		 * @return further configuration options
		 * @throws IllegalArgumentException if the address is invalid
		 */
		public SecondClause<P> at(String key, URL url) throws IllegalArgumentException;
		
		/**
		 * Configures the address of a given service instance.
		 * @param the key of the instance
		 * @param address the address of the corresponding service endpoint
		 * @return further configuration options
		 * @throws IllegalArgumentException if the address is invalid
		 */
		public SecondClause<P> at(String key, URI url) throws IllegalArgumentException;
		
	}
	
	/**
	 * The second clause of the DSL.
	 * 
	 * @author Fabio Simeoni
	 *
	 * @param <P> the type of service proxies
	 */
	public static interface SecondClause<P> {
		
		/**
		 * Configures the timeout for the proxy.
		 * @param timeoutDuration the duration of the timeout
		 * @param timeoutUnit the time unit of the timeout
		 * @return further configuration options
		 */
		public FinalClause<P> withTimeout(int timeoutDuration, TimeUnit timeoutUnit);
		
		/**
		 * Set a configuration property for the proxy.
		 * @param name the name of the property
		 * @param value the value of the property
		 * @return further configuration options
		 * 
		 * @param <T> the type of the property value
		 */
		public <T> SecondClause<P> with(String name, T value);
		
		/**
		 * Set a configuration property for the proxy.
		 * @param name the property
		 * @return further configuration options
		 */
		public SecondClause<P> with(Property<?> property);
		
		/**
		 * Returns a configured proxy.
		 * @return the proxy
		 */
		public P build();
	}
	
	/**
	 * The final clause of the DSL.
	 * 
	 * @author Fabio Simeoni
	 *
	 * @param <P> the type of service proxies
	 */
	public static interface FinalClause<P> {
		
		/**
		 * Returns a configured proxy.
		 * @return the proxy
		 */
		public P build();
	
	}
	
	
}
