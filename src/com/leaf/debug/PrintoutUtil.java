package com.leaf.debug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class PrintoutUtil {
	private static final int INDENT_FACTOR = 4;
	
	public static void print(String tag, String msg, byte[] json) {
		try {
			JSONObject jsonObj = new JSONObject(new String(json));
			Log.i( tag,  msg + ":"+ jsonObj.toString(INDENT_FACTOR));
		} catch (JSONException e) {
			throw new IllegalArgumentException( " invalid json: " + new String(json));
		}
	}
	
	public static void print(String tag, String msg, JSONArray jsonArray) {
		try {
			Log.i( tag, msg + ": " + jsonArray.toString(INDENT_FACTOR));
		} catch (JSONException e) {
			throw new IllegalArgumentException( " invalid json: " );
		}
	}
	
	public static void print(String tag, String msg, JSONObject jsonObject) {
		try {
			Log.i( tag, msg + ": " + jsonObject.toString(INDENT_FACTOR));
		} catch (JSONException e) {
			throw new IllegalArgumentException( " invalid json: " );
		}
	}
}
