package com.leaf.leafData.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Request {
	public static enum Method { GET, POST, PATCH, DELETE }

	private final Method method;
	private final String requestUrlString;
	private final Map<String, List<String>> headers;
	private final byte[] body;

	/**
	 * Creates a Request.
	 * @param method the request method, one of GET, POST, PUT, DELETE
	 * @param requestUrl the URL to which the request is made
	 * @param headers the request headers, a Map<String, List<String>>
	 * @param body the body of the request
	 */
	public Request(Method method, String requestUrlString, Map<String, List<String>> headers, byte[] body) {
		this.method = method;
		this.requestUrlString = requestUrlString;
		this.headers = (null != headers) ? headers : new HashMap<String, List<String>>();
		this.body = body;
	}

	/**
	 * @return method as a Method
	 */
	public Method getMethod() { return method; }

	/**
	 * @return the request url
	 */
	public String getRequestUrlString() { return requestUrlString; }

	/**
	 * @return headers as a Map<String, List<String>>
	 */
	public Map<String, List<String>> getHeaders() { return headers; }
	
	
	/**
	 * @return body as byte array
	 */
	public byte[] getBody() { return body; }
}
