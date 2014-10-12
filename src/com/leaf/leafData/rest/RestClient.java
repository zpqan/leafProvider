package com.leaf.leafData.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;



public class RestClient {
	public static final String TAG = RestClient.class.getSimpleName();

	public static final String HTTP_GET = "GET";
	public static final String HTTP_POST = "POST";
	public static final String HTTP_PATCH = "PATCH";
	public static final String HTTP_DELETE = "DELETE";
    public static final int BUFF_SIZE = 1024;

    private HttpClient httpclient = new DefaultHttpClient();

	private String siteId;


	/**
	 * Takes an InputStream and writes it to a byte array.
	 * 
	 * @param in
	 *            the input stream to be read
	 * @return a byte[] containing the contents of the input stream
	 * @throws IOException
	 */
    private static byte[] readStream(InputStream in) throws IOException {
        byte[] buf = new byte[BUFF_SIZE];
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFF_SIZE);
        int count;
        while ((count = in.read(buf)) != -1) {
            out.write(buf, 0, count);
        }

        return out.toByteArray();
    }

	/**
	 * Returns the singleton. Ensures that instance is not null.
	 * 
	 * @return a RestClient which is not null
	 */
	public static RestClient getInstance() {
		synchronized(RestClient.class) {
			if ( instance == null) {
				instance = new RestClient();
			}
		}

		return instance;
	}

    private static RestClient instance;

	/**
	 * Executes a request to the URL specified in the request and awaits a
	 * response. If no response is received, creates one with all blank fields.
	 * 
	 * @param request
	 *            request to be executed
	 * @return the response received, or a blank response if no response is
	 *         received.
	 */
	public Response execute(Request request) {
		Response response = null;
		try {
			String urlString = request.getRequestUrlString();
			// TODO: fix the harded site_id
			siteId = LeafRest.SITE_ID;
			switch (request.getMethod()) {
                case GET:
                	response = executeHttpGetRequest(urlString);
                	break;
                case DELETE:
                	response = executeHttpDeleteRequest(urlString);
                	break;
                 case POST: 
                	 response = executeHttpPostRequest(urlString, request.getBody());
                	 break;
                case PATCH:
                	response = executeHttpPatchRequest(urlString, request.getBody());
                	break;
               default:
                    break;
			}


		}
        catch (IOException ex) {
        	Log.e("RestClient", ex.getMessage());
		}
        finally {
		}

		return response;
	}
	
	private Response executeHttpGetRequest(String urlString) throws IOException {
		HttpGet httpGet = new HttpGet(urlString);
		addAuthenticationHeaders(httpGet, HTTP_GET, urlString);
		return  getResponse(httpclient.execute(httpGet));
	}
	
	
	private Response executeHttpPostRequest(String urlString, byte[] body) throws IOException {
		HttpPost httpPost = new HttpPost(urlString);
		addAuthenticationHeaders(httpPost, HTTP_POST, urlString);
		if ( body != null ) {
			HttpEntity entity = createHttpEntity(body);
			httpPost.setEntity(entity);
			httpPost.addHeader(LeafRest.CONTENT_TYPE_TOKEN, LeafRest.CONTENT_TYPE_VALUE);
		}
		return  getResponse(httpclient.execute(httpPost));
	}
	
	private Response executeHttpPatchRequest(String urlString, byte[] body) throws IOException {
		HttpPatch httpPatch = new HttpPatch(urlString);
		addAuthenticationHeaders(httpPatch, HTTP_PATCH, urlString);
		if ( body != null ) {
			HttpEntity entity = createHttpEntity(body);
			httpPatch.setEntity(entity);
			httpPatch.addHeader(LeafRest.CONTENT_TYPE_TOKEN, LeafRest.CONTENT_TYPE_VALUE);
		}
		return  getResponse(httpclient.execute(httpPatch));
	}

	private Response executeHttpDeleteRequest(String urlString) throws IOException {
		HttpDelete httpDelete = new HttpDelete(urlString);
		addAuthenticationHeaders(httpDelete, HTTP_DELETE, urlString);
		return  getResponse(httpclient.execute(httpDelete));
	}
	
	private HttpEntity createHttpEntity(byte[] body) throws IOException {
		 StringEntity entity = new StringEntity(new String(body)); 
         entity.setContentEncoding("UTF-8"); 
         entity.setChunked(true); 
         entity.setContentType("application/json"); 
         return entity;
	}
	
	
	private Response getResponse(HttpResponse httpResponse) throws IOException {
		byte[] body = null;
	    HttpEntity entity = httpResponse.getEntity();
	
	    if (entity != null) {
	        InputStream instream = entity.getContent();
	        try {
	        	body = readStream(instream);
	        } finally {
	            instream.close();
	        }
	    }
	    return new Response(httpResponse.getStatusLine().getStatusCode(),
			getResponseHeaders(httpResponse.getAllHeaders()), body);

	}
	private Map<String, List<String>> getResponseHeaders(Header[] headers) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for ( Header h : headers ) {
			List<String> l = map.get(h.getName());
			if ( l == null) {
				l = new ArrayList<String>();
				map.put(h.getName(), l);
			}
			l.add(h.getValue());
		}
		return map;
	}
	
	private void addAuthenticationHeaders(HttpRequest request, String restMethod, String urlString) {
		try {
			URL url = new URL(urlString);
			String[] path = url.getPath().split("/");
			String timeStamp = System.currentTimeMillis()/1000 + "";
			String signature = String.format("%s,%s,%s,%s", timeStamp, restMethod, path[1], siteId);	
			Log.d(TAG, "signature = " + signature);
			request.addHeader("leaf-api-site-id", LeafRest.SITE_ID);		
			request.addHeader("leaf-api-timestamp", timeStamp);
			request.addHeader("leaf-api-signature-sha512", LeafRest.encode(signature));
			if (!restMethod.equals(HTTP_DELETE)) {
				request.addHeader(LeafRest.CONTENT_TYPE_TOKEN, LeafRest.CONTENT_TYPE_VALUE);
			}
			request.addHeader(LeafRest.ACCEPT_TYPE_TOKEN, LeafRest.ACCEPT_TYPE_VALUE);
		} catch (MalformedURLException e) {
			Log.e("RestClient", e.getMessage());
		}
	}
	
}
