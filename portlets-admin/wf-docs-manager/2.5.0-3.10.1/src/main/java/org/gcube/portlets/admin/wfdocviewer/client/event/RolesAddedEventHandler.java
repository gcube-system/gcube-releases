package org.gcube.portlets.admin.wfdocviewer.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface RolesAddedEventHandler extends EventHandler {
  void onAddRoles(RolesAddedEvent rolesAddedEvent);
}
