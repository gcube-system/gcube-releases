/**
 * 
 */
package org.gcube.portlets.user.td.gwtservice.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.gcube.portlets.user.td.gwtservice.server.storage.FilesStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Returns a chart file from storage
 * 
 * @author "Giancarlo Panichi" <a
 *         href="mailto:g.panichi@isti.cnr.it">g.panichi@isti.cnr.it</a>
 *
 */
public class RetrieveChartFileServlet extends HttpServlet {
	private static final long serialVersionUID = -1649268678733476057L;
	
	private static Logger logger = LoggerFactory
			.getLogger(RetrieveChartFileServlet.class);
	
	private static final String ATTRIBUTE_STORAGE_URI = "storageURI";
	private static final String IMAGE_MIME_TYPE = "image/jpeg";
	private static final int BUFSIZE = 4096;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}

	protected void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			logger.info("RetrieveChartFileServlet");
			long startTime = System.currentTimeMillis();

			HttpSession session = request.getSession();

			if (session == null) {
				logger.error("Error getting the session, no session valid found: "
						+ session);
				response.sendError(
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"ERROR-Error getting the user session, no session found "
								+ session);
				return;
			}
			logger.debug("RetrieveChartFileServlet session id: "
					+ session.getId());

			SessionUtil.getAslSession(session);
			String uri = (String) request.getParameter(ATTRIBUTE_STORAGE_URI);
			logger.debug("Request storage uri: "+uri);
			
			if(uri==null|| uri.isEmpty()){
				logger.error("Error getting request uri: "
						+ uri);
				response.sendError(
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"ERROR-Error getting request uri: "
								+ session);
				return;
			}
			
			FilesStorage storage = new FilesStorage();
			InputStream in = storage.retrieveImputStream(uri);
			OutputStream out = response.getOutputStream();

			response.setContentType(IMAGE_MIME_TYPE);

			byte[] byteBuffer = new byte[BUFSIZE];

			int length = 0;
			while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
				out.write(byteBuffer, 0, length);
			}
			response.setStatus(HttpServletResponse.SC_OK);
			in.close();
			out.close();

			logger.trace("Response in "
					+ (System.currentTimeMillis() - startTime));

		} catch (Throwable e) {
			logger.error("Error retrieving file from storage: "
					+ e.getLocalizedMessage());
			e.printStackTrace();
			response.sendError(
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Error retrieving file from storage: "
							+ e.getLocalizedMessage());
			return;
		}
	}
}
