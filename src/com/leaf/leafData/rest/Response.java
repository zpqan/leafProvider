package com.leaf.leafData.rest;

import java.util.List;
import java.util.Map;


public class Response {
    private final int status;
    private final Map<String, List<String>> headers;
    private final byte[] body;

	/**
	 * Ctor for a Response object
	 * 
	 * @param status
	 *            response status indicated by an int
	 * @param headers
	 *            a Map<String, List<String>> of response headers
	 * @param body
	 *            a byte array
	 */
    public Response(int status,  Map<String, List<String>> headers, byte[] body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

	/**
	 * @return response status as an int
	 */
    public int getStatus() { return status; }

	/**
	 * @return response headers as a Map<String, List<String>>
	 */
    public Map<String, List<String>> getHeaders() { return headers; }

	/**
	 * @return the body of the response as a byte array
	 */
    public byte[] getBody() { return body; }
}
