package org.gcube.common.clients.builders;

import java.net.URI;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.gcube.common.clients.config.Property;

/**
 * Defines a DSL for proxy builders of stateless services.
 * 
 * @author Fabio Simeoni
 *
 */
public class StatelessBuilderAPI {

	/**
	 * The first clause of the DSL.
	 * 
	 * @author Fabio Simeoni
	 *
	 * @param <P> the type of service proxies
	 */
	public static interface Builder<P> {
		
		
		/**
		 * Configures the address of a given service instance.
		 * @param address the address of the corresponding service endpoint
		 * @return further configuration options
		 * @throws IllegalArgumentException if the address is invalid
		 */
		
		public SecondClause<P> at(String host, int port) throws IllegalArgumentException;
		
		/**
		 * Configures the address of a given service endpoint.
		 * @param address the address of the endpoint
		 * @return further configuration options
		 * @throws IllegalArgumentException if the address is invalid
		 */
		public SecondClause<P> at(URL address) throws IllegalArgumentException;
		
		/**
		 * Configures the address of a given service endpoint.
		 * @param address the address of the endpoint
		 * @return further configuration options
		 * @throws IllegalArgumentException if the address is invalid
		 */
		public SecondClause<P> at(URI address) throws IllegalArgumentException;
		
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
		public <T> Builder<P> with(String name, T value);
		
		/**
		 * Set a configuration property for the proxy.
		 * @param name the property
		 * @return further configuration options
		 */
		public Builder<P> with(Property<?> property);
		
		/**
		 * Configures the timeout for the proxy.
		 * @param timeoutDuration the duration of the timeout
		 * @param timeoutUnit the time unit of the timeout
		 * @return further configuration options
		 */
		public P build();
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
