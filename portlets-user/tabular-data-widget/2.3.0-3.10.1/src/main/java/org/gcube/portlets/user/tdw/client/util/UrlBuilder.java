/**
 * 
 */
package org.gcube.portlets.user.tdw.client.util;

import com.google.gwt.http.client.URL;

/**
 * @author "Federico De Faveri defaveri@isti.cnr.it"
 *
 */
public class UrlBuilder {

	protected StringBuilder url = new StringBuilder();
	protected boolean first = true;

	public void clear()
	{
		url = new StringBuilder();
		first = true;
	}

	public void addParameter(String name, String value)
	{
		if (value!=null) {
			if (first) first = false;
			else url.append('&');

			url.append(URL.encodeQueryString(name));
			url.append('=');
			url.append(URL.encodeQueryString(value));
		}
	}

	public String toString()
	{
		return url.toString();
	}

}
