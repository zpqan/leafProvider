package com.leaf.leafData.util;

import java.lang.reflect.Constructor;
import java.util.List;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.leaf.leafData.dataModel.BaseModel;
import com.leaf.leafData.rest.Request;
import com.leaf.leafData.rest.Response;
import com.leaf.leafData.rest.RestClient;
import com.leaf.leafData.rest.Request.Method;

public class SyncUtil {
	private static final String TAG = SyncUtil.class.getSimpleName();
	
//	public static <T extends BaseModel> int updateLocalFromServerModifiedRecords(Context ctx, Bundle extras, 
//			Uri uri, String url, Class<T> dataType) {
//		String urlwithQury = createGetAllUrl(extras, url);
//		return updateLocalFromServer(ctx, uri, urlwithQury, dataType, LeafDbHelper.CLEAN);
//	}
		
	public static  <T extends BaseModel> int getChangedFromServer( String url, int syncFlag, List<BaseModel> list, Class<T> dataType  )  {
       	Request request = new Request(Method.GET, url, null, null);
        Response response= RestClient.getInstance().execute(request);
        int statusCode = response.getStatus();
        if (statusCode == HttpStatus.SC_OK) {
        	try {
				getList(response.getBody(), list,syncFlag, dataType );
			} catch (JSONException e) {
				Log.i(TAG, "getFromServer() failed", e);
			}
        }
        return statusCode;
	}
	private static <T extends BaseModel> void getList (byte[] json, List<BaseModel> list, 
			int syncFlag, Class<T> dataType ) throws JSONException  {
        JSONArray jsonArray;
        jsonArray = new JSONArray(new String(json));
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
			try {
				Constructor<T> constructor;
				constructor = dataType.getConstructor(JSONObject.class);
				T t = constructor.newInstance(jsonObj);
				t.setSyncFlag(syncFlag);
				list.add(t);
			} catch (Exception e) {
				throw new IllegalArgumentException(" cannot create " +
						dataType.getName() + " from json: " + jsonObj.toString());
			} 						
        }
    }	
}
