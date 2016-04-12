/**
 * 
 */
package org.gcube.portlets.user.workspace.client.workspace;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Federico De Faveri defaveri@isti.cnr.it
 *
 */
public enum GWTWorkspaceItemAction implements IsSerializable{

	CREATED,
	RENAMED,
	MOVED,
	CLONED;
}
