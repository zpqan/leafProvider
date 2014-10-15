package com.leaf.client.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.leaf.leafclient.dataModel.BaseModel;
import com.leaf.leafclient.dataModel.LineItem;
import com.leaf.leafclient.dataModel.LineItemModifier;

import android.database.Cursor;
import android.util.Log;

public class DataModelUtils {
	private static final String TAG = "Utils";

	public static Object getObjectFromJson(JSONObject jsonObj, String name) {
		if (jsonObj.has(name))
			try { return jsonObj.get(name); }
		catch (JSONException e) {
			Log.i(TAG, "Failed getting JSON Object " + name, e);
		}
		return null;
	}

	public static String getStringFromJson(JSONObject jsonObj, String name) {
		if (jsonObj!= null && jsonObj.has(name))
			try { return jsonObj.getString(name); }
		catch (JSONException e) {
			Log.i(TAG, "Failed getting JSON string " + name, e);
		}
		return null;
	}

	public static Integer getIntegerFromJson(JSONObject jsonObj, String name) {
		if (jsonObj!= null && jsonObj.has(name))
			try { return jsonObj.getInt(name); }
		catch (JSONException e) {
			Log.i(TAG, "Failed getting JSON Integer " + name, e);
		}
		return null;
	}

	public static Boolean getBooleanFromJson(JSONObject jsonObj, String name) {
		if (jsonObj!= null && jsonObj.has(name))
			try { 
				boolean value = jsonObj.getBoolean(name);

				return Boolean.valueOf(value); }
		catch (JSONException e) {
			Log.i(TAG, "Failed getting JSON Boolean " + name, e);
		}
		return null;
	}

	public static BigDecimal getBigDecimalFromJson(JSONObject jsonObj, String name) {
		if (jsonObj!= null && jsonObj.has(name))
			try { 
				return new BigDecimal(jsonObj.getString(name));
			} catch (JSONException e) {
				Log.i(TAG, "Failed getting JSON BigDecimal " + name, e);
			}
		return null;
	}
	
	public static Float getFloatFromJson(JSONObject jsonObj, String name) {
		if (jsonObj!= null && jsonObj.has(name))
			try { 
				return Float.valueOf(jsonObj.getString(name));
			} catch (JSONException e) {
				Log.i(TAG, "Failed getting JSON Float " + name, e);
			}
		return null;
	}


	public static Date getDateFromJson(JSONObject jsonObj, String name) {
		if (jsonObj!= null && jsonObj.has(name))
			try { 
				String s = jsonObj.getString(name);
				Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz").parse(s);
				return date;
			} catch (JSONException e) {
				Log.d(TAG, "Failed getting JSON Date " + name);
			} catch (ParseException e) {
				Log.d(TAG, "Failed parsing Date " + name);
			} catch (Exception e) {
				Log.d(TAG, "Failed getting Date from JSON" + name);
			}
		return null;
	}

	public static JSONObject getJSONObjectFromJson(JSONObject jsonObj, String name) {
		if (jsonObj!= null && jsonObj.has(name))
			try {
				return jsonObj.getJSONObject(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return null;
	}

	public static JSONArray getArrayFromJson(JSONObject jsonObj, String name) {
		JSONArray jsonArray = null;
		if (jsonObj!= null && jsonObj.has(name))
			try {
				jsonArray = jsonObj.getJSONArray(name);
			} catch (JSONException e) {
				
			}
		if ( jsonArray == null ) {
			jsonArray = new JSONArray();
			try {
				jsonObj.put(name, jsonArray);
			} catch (JSONException e) {
			}
		}
		return jsonArray;
	}

	public String toJson(Integer integer) {
		return "" + Integer.valueOf(integer);
	}

	public String toJson(String string) {
		return "\"" + string + "\"";
	}

	public String toJson(Boolean b) {
		return "" + Boolean.valueOf(b);
	}

	public String toJson(BigDecimal bigDecimal) {
		return "" + bigDecimal.toString();
	}

	public String toJson(Date date) {
		return "\"" + date.toString()+ "\"";
	}

	public static Boolean getCursorBoolean(Cursor cursor,
			String columnName, boolean defaultValue) {

		int columnIndex = cursor.getColumnIndex(columnName);
		if (columnIndex == -1) {
			Log.d(TAG, "no such column: " + columnName);
			return Boolean.valueOf(defaultValue);
		}
		int value = cursor.getInt(columnIndex);

		Boolean boo = Boolean.valueOf(value != 0);

		return boo;
	}
	
	public static Boolean getCursorBoolean(Cursor cursor,
			String columnName) {

		int columnIndex = cursor.getColumnIndex(columnName);
		if (columnIndex == -1) {
			Log.d(TAG, "no such column: " + columnName);
			return null;
		}
		int value = cursor.getInt(columnIndex);

		Boolean boo = Boolean.valueOf(value != 0);

		return boo;
	}


	public static Integer getCursorInteger(Cursor cursor,
			String columnName, int defaultValue) {
		int columnIndex = cursor.getColumnIndex(columnName);
		if (columnIndex == -1) {
			Log.d(TAG, "no such column: " + columnName);
			return Integer.valueOf(defaultValue);
		}

		if (cursor.isNull(columnIndex))
			return Integer.valueOf(defaultValue);
		else
			return Integer.valueOf(cursor.getInt(columnIndex));
	}

	public static Integer getCursorInteger(Cursor cursor, String columnName) {
		int columnIndex = cursor.getColumnIndex(columnName);
		if (columnIndex == -1) {
			Log.d(TAG, "no such column: " + columnName);
			return null;
		}

		if (cursor.isNull(columnIndex))
			return null;
		else
			return Integer.valueOf(cursor.getInt(columnIndex));
	}

	public static Float getCursorFloat(Cursor cursor, String columnName) {
		int columnIndex = cursor.getColumnIndex(columnName);
		if (columnIndex == -1) {
			Log.d(TAG, "no such column: " + columnName);
			return null;
		}

		if (cursor.isNull(columnIndex))
			return null;
		else
			return Float.valueOf(cursor.getFloat(columnIndex));
	}
	
	public static BigDecimal getCursorBigDecimal(Cursor cursor, String columnName) {
		int columnIndex = cursor.getColumnIndex(columnName);
		if (columnIndex == -1) {
			Log.d(TAG, "no such column: " + columnName);
			return null;
		}

		if (cursor.isNull(columnIndex))
			return null;
		else
			return new BigDecimal(cursor.getString(columnIndex));
	}

	public static String getCursorString(Cursor cursor, String columnName) {
		int columnIndex = cursor.getColumnIndex(columnName);
		if (columnIndex == -1) {
			Log.d(TAG, "no such column: " + columnName);
			return null;
		}

		if (cursor.isNull(columnIndex))
			return null;
		else
			return cursor.getString(columnIndex);
	}
	
	public static JSONArray modelListToJsonArray(BaseModel[] models) {
		JSONArray jsonArray = new JSONArray();
		if ( models!= null ) {
			for ( BaseModel model : models) {
				jsonArray.put(model.getJsonObject());
			}
		}
		return jsonArray;
	}

//	public static Object getCursorObject(Cursor cursor, String columnName) {
//		int columnIndex = cursor.getColumnIndex(columnName);
//		if (columnIndex == -1) {
//			Log.d(TAG, "no such column: " + columnName);
//			return null;
//		}
//
//		if (cursor.isNull(columnIndex))
//			return null;
//		else
//			return cursor.getBlob(columnIndex);
//	}
//


	//	public static void jsonObjectPutBoolean(JSONObject jsonObject,
	//			String name, Boolean booleanValue) {
	//		if (booleanValue != null) {
	//			try {
	//				jsonObject.put(name, Boolean.valueOf(booleanValue));
	//			} catch (JSONException e) {
	//	            Log.i(TAG, "Failed jsonObjectPutBoolean " + name, e);
	//			}
	//		}
	//	}
	//
	//	public static void jsonObjectPutInteger(JSONObject jsonObject, String name,
	//			Integer intObject) {
	//		if (intObject != null) {
	//			try {
	//				jsonObject.put(name, Integer.valueOf(intObject));
	//			} catch (JSONException e) {
	//	            Log.i(TAG, "Failed jsonObjectPutInteger " + name, e);
	//			}
	//		}
	//	}
	//
	//	public static void jsonObjectPutBigDecimal(JSONObject jsonObject,
	//			String name, Object bigDecimal) {
	//		if (bigDecimal != null) {
	//			try {
	//				jsonObject.put(name, bigDecimal);
	//			} catch (JSONException e) {
	//	            Log.i(TAG, "Failed jsonObjectPutBigDecimal " + name, e);
	//			}
	//		}
	//	}
	//
	//	public static void jsonObjectPutString(JSONObject jsonObject,
	//			String name, String string) {
	//		if (string != null) {
	//			try {
	//				jsonObject.put(name, string);
	//			} catch (JSONException e) {
	//	            Log.i(TAG, "Failed jsonObjectPutString " + name, e);
	//			}
	//		}
	//	}

	public static void addToJson(JSONObject jsonObject, String name, String string) {
		if (string != null) {
			try {
				jsonObject.put(name, string);
			} catch (JSONException e) {
				Log.i(TAG, "Failed jsonObjectPutString " + name, e);
			}
		}
	}

	public static void addToJson(JSONObject jsonObject, String name, Boolean booleanObject) {
		if (booleanObject != null) {
			try {
				jsonObject.put(name, Boolean.valueOf(booleanObject));
			} catch (JSONException e) {
				Log.i(TAG, "Failed jsonObjectPutBoolean " + name, e);
			}
		}
	}

	public static void addToJson(JSONObject jsonObject,
			String name, BigDecimal bigDecimal) {
		if (bigDecimal != null) {
			try {
				jsonObject.put(name, bigDecimal);
			} catch (JSONException e) {
				Log.i(TAG, "Failed jsonObjectPutBigDecimal " + name, e);
			}
		}
	}

	public static void addToJson(JSONObject jsonObject, String name, Integer intObject) {
		if (intObject != null) {
			try {
				jsonObject.put(name, Integer.valueOf(intObject));
			} catch (JSONException e) {
				Log.i(TAG, "Failed jsonObjectPutInteger " + name, e);
			}
		}
	}
	
	public static void addToJson(JSONObject jsonObject, String name, Float floatObject) {
		if (floatObject != null) {
			try {
				jsonObject.put(name, Float.valueOf(floatObject));
			} catch (JSONException e) {
				Log.i(TAG, "Failed jsonObjectPutInteger " + name, e);
			}
		}
	}
	public static void put(JSONObject jsonObj, String name, Object value) {
		try { 
			jsonObj.put(name, value); }
		catch (JSONException e) {
			Log.i(TAG, "Failed getting JSON string " + name, e);
		}
	}
	public static JSONObject getJSONObjectFromArray(JSONArray jsonArray, int index) {
		if (jsonArray != null &&  jsonArray.length() > index)
			try {
				return jsonArray.getJSONObject(index);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return null;
	}
//	public static LineItem[] removeUnChangedLineItems(LineItem[] models) {
//		List<LineItem> list = new ArrayList<LineItem>(models.length);
//		for (LineItem model : models) {
//			model.makePatch();
//			if ( model.isIncludedInPatchCall() ){
//				list.add(model);
//			}
//		}
//		return list.toArray(new LineItem[list.size()]);
//	}
//	public static LineItemModifier[] removeUnChangedLineItemModifiers(LineItemModifier[] models) {
//		List<LineItemModifier> list = new ArrayList<LineItemModifier>(models.length);
//		for (LineItemModifier model : models) {		
//			if ( model.getDeleted() ||  model.getDirty() || model.getId() == null ){
//				list.add(model);
//			}
//		}
//		return list.toArray(new LineItemModifier[list.size()]);
//	}
}
