package com.leaf.leafData.rest;

import java.nio.charset.Charset;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import android.util.Log;

public class LeafRest {
	public static final String TAG = LeafRest.class.getSimpleName();

	private LeafRest() {}



	public static final String LEAF_SANDBOX_HOST = "sandbox.leaf.me";
	//public static final String LEAF_TEST_HOST = "api.leaftest.me";
	public static final String LEAF_TEST_HOST = "leafsdk-37.qa.leaftest.me";

	public static final String LEAF_NORMAL_HOST = "api.leaf.me";
//	https://api.leafsandbox.me/
	// use LEAF_NORMAL for real, LEAF_SANDBOX for accessing sandbox data
	public static final String LEAF_HOST = "api.leaftest.me";		
	public static final String LEAF_URL = "https://" + LEAF_HOST;		


	public static final String CONTENT_TYPE_TOKEN = "Content-type";
	public static final String CONTENT_TYPE_VALUE = "application/json";
	public static final String ACCEPT_TYPE_TOKEN = "Accept";
	public static final String ACCEPT_TYPE_VALUE = "application/json";
//	public static final String CONTENT_TYPE_VALUE = "application/vnd.leaf-me+json; version=1.0";

	private static final String TOKEN = "458140DE-CC9B-426B-B659-171D31211AD0";
	public static final String SITE_ID = "1096";
	private static final String HMAC_SHA512_ALGORITHM = "HmacSHA512";


	public static String encode(String data) {
		String result = null;
		try {
			SecretKeySpec signingKeySpec = new SecretKeySpec(TOKEN.getBytes(Charset.forName("UTF-8")), HMAC_SHA512_ALGORITHM);

			Mac mac = Mac.getInstance(HMAC_SHA512_ALGORITHM);
			mac.init(signingKeySpec);

			byte[] rawHmac = mac.doFinal(data.getBytes(Charset.forName("UTF-8")));
			result = new String(Base64.encodeBase64(rawHmac), Charset.forName("UTF-8"));

			Log.d(TAG.concat(".encode"), "data: " + data);
			Log.d(TAG.concat(".encode"), "result: " + result);
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Calculate HmacSHA512 failed: " + e.getMessage());
		}

		return result;
	}

}
