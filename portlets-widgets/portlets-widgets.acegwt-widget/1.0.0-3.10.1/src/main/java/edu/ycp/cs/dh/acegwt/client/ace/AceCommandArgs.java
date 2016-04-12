package edu.ycp.cs.dh.acegwt.client.ace;

import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Ace command's argument could be either string or string-to-string map.
 */
public class AceCommandArgs {
	private final Object value;
	
	/**
	 * Create map argument. In case param data is null map will be empty.
	 *
	 * @param data map
	 */
	public AceCommandArgs(Map<String, String> data) {
		value = JavaScriptObject.createObject();
		if (data != null)
			for (Map.Entry<String, String> entry : data.entrySet())
				with(entry.getKey(), entry.getValue());
	}
	
	/**
	 * Create text argument.
	 * 
	 * @param value text argument
	 */
	public AceCommandArgs(String value) {
		this.value = value;
	}
	
	/**
	 * Add key-value pair to map.
	 *
	 * @param argKey key
	 * @param argValue value
	 * @return AceCommandArgs
	 */
	public native AceCommandArgs with(String argKey, String argValue) /*-{
		this.@edu.ycp.cs.dh.acegwt.client.ace.AceCommandArgs::value[argKey] = argValue;
		return this;
	}-*/;
	
	/**
	 * Give inner value.
	 * @return string or map depending on used constructor
	 */
	public Object getValue() {
		return value;
	}
}
