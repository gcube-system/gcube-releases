/**
 * 
 */
package org.gcube.vremanagement.executor.client.plugins.query.filter;

import org.gcube.resources.discovery.client.queries.api.SimpleQuery;

/**
 * @author Luca Frosini (ISTI - CNR) http://www.lucafrosini.com/
 *
 */
public interface ServiceEndpointQueryFilter {

	public void filter(SimpleQuery simpleQuery);
	
}
