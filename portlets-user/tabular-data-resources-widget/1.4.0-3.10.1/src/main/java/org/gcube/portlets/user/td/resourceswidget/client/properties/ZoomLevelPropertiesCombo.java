package org.gcube.portlets.user.td.resourceswidget.client.properties;


import org.gcube.portlets.user.td.resourceswidget.client.store.ZoomLevelElement;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * 
 * @author giancarlo email: <a
 *         href="mailto:g.panichi@isti.cnr.it">g.panichi@isti.cnr.it</a>
 *
 */
public interface ZoomLevelPropertiesCombo extends PropertyAccess<ZoomLevelElement> {

	@Path("id")
	ModelKeyProvider<ZoomLevelElement> id();

	LabelProvider<ZoomLevelElement> label();

}
