/**
 * 
 */
package org.gcube.portlets.user.statisticalmanager.client.util;

import com.google.gwt.event.shared.HandlerManager;

/**
 * @author ceras
 *
 */
public class EventBusProvider {

	private static HandlerManager eventBus=null;
	
	public static HandlerManager getInstance() {
		if (eventBus==null)
			eventBus = new HandlerManager(null);
		return eventBus;
	}

}
