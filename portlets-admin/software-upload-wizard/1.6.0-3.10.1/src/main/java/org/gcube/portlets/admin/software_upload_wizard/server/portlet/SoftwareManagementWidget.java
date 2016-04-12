package org.gcube.portlets.admin.software_upload_wizard.server.portlet;

import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.gcube.portal.custom.scopemanager.scopehelper.ScopeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoftwareManagementWidget extends GenericPortlet {

	private static final Logger log = LoggerFactory.getLogger(SoftwareManagementWidget.class);

	/**
	 * JSP folder name
	 */
	public static final String JSP_FOLDER = "/WEB-INF/jsp/";

	/**
	 * 
	 */
	public static final String VIEW_JSP = JSP_FOLDER + "SoftwareManagementWidget_view.jsp";

	/**
	 * @param request
	 *            .
	 * @param response
	 *            .
	 * @throws IOException .
	 * @throws PortletException .
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {

		log.trace("SoftwareManagementWidget loading from JSP: " + VIEW_JSP);

		log.trace("setting context using ScopeHelper");
		ScopeHelper.setContext(request);

		log.trace("passing to the render");
		PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(VIEW_JSP);
		rd.include(request, response);
	}
}
