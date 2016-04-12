/**
 *
 */
package org.gcube.portlets.user.gcubegisviewer.server;


/**
 * The Class ServerParameters.
 *
 * @author Francesco Mangiacrapa francesco.mangiacrapa@isti.cnr.it
 * Feb 2, 2016
 */
public class ServerParameters {

	protected String url;
	protected String user;
	protected String password;

	/**
	 * Instantiates a new server parameters.
	 */
	public ServerParameters(){}

	/**
	 * Instantiates a new server parameters.
	 *
	 * @param url the url
	 * @param user the user
	 * @param password the password
	 */
	public ServerParameters(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ServerParameters [url=");
		builder.append(url);
		builder.append(", user=");
		builder.append(user);
		builder.append(", password=");
		builder.append(password);
		builder.append("]");
		return builder.toString();
	}
}
