package com.leaf.leafData.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.leaf.client.util.ContentProviderUtil;
import com.leaf.client.util.SyncUtil;
import com.leaf.leafData.ClientApp;
import com.leaf.leafData.DataModel.BaseModel;
import com.leaf.leafData.DataModel.Order;
import com.leaf.leafData.provider.LeafContract;
import com.leaf.leafData.provider.LeafDbHelper;
import com.leaf.leafData.rest.LeafRest;
import com.leaf.leafData.rest.Request;
import com.leaf.leafData.rest.Request.Method;
import com.leaf.leafData.rest.Response;
import com.leaf.leafData.rest.RestClient;


public class OrderProcessor {
	private final String TAG = OrderProcessor.class.getSimpleName();

	private static final String urlString = LeafRest.LEAF_URL + Order.URLPATH;

	public OrderProcessor() {

	}
	
	public int processRestCall(Context ctx, Bundle extras) {
		try {
			String urlwithQury = createGetAllUrl(extras);
			List<BaseModel> models = new ArrayList<BaseModel>();
			int statusCode = SyncUtil.getChangedFromServer(urlwithQury,
					LeafDbHelper.CLEAN, models, Order.class);
			if (statusCode != HttpStatus.SC_OK) {
				Log.e(TAG, "rest call failed with error code: " + statusCode);
				return statusCode;
			}
			Log.i(TAG, "modified order size: " + models.size());
			Log.i(TAG, "user id: " + ((Order) models.get(0)).getUser_id()
					+ " id: " + models.get(0).getId());
			ContentProviderUtil.updateContentProvider(ctx,
					LeafContract.Order.CONTENT_URI, models);
			return statusCode;
		} catch (Exception e) {
			Log.e(TAG, "failed processing json", e);
		}
		return -1;
	}
	
	private String createGetAllUrl(Bundle extras) {
		String params = "";
        if (extras != null) {
            params = extras.getString(ClientApp.REST_PARAMS);
        }      
        String urlTemp = (params == null || params.length()==0) ? urlString + "?" : urlString + params + "&";
        return urlTemp + "per_page=500";

	}
	private String createGetAllDeletedUrl(Bundle extras) {
		String params = "";
        if (extras != null) {
            params = extras.getString(ClientApp.REST_PARAMS);
        }      
        String urlTemp = (params == null || params.length()==0) ? urlString + "?" : urlString + params + "&";
        return urlTemp + "per_page=500&deleted=true";

	}

	private int updateChangesFromServer(Context ctx, Bundle extras ) {
	      try {
	    	  	String urlwithQury = createGetAllUrl(extras);
	    	  	List<BaseModel> models = new ArrayList<BaseModel>();
	    	  	Map<String, Order> newLocalOrderMap = getNewLocalOrders(ctx);
	            int statusCode = SyncUtil.getChangedFromServer(urlwithQury, LeafDbHelper.CLEAN , models, Order.class);
	            if (statusCode != HttpStatus.SC_OK) {
	            	return statusCode;
	            }
	            Log.i(TAG, "modified order size: " + models.size());
	            for ( BaseModel order : models) {
	            		mergeLocalNewOrder((Order)order, newLocalOrderMap);
	            }
	            ContentProviderUtil.updateContentProvider(ctx, LeafContract.Order.CONTENT_URI, models);
	            models.clear();
	            /** we get all deleted records from the server and then delete them **/
	            urlwithQury = createGetAllDeletedUrl(extras);
	            statusCode = SyncUtil.getChangedFromServer(urlwithQury, LeafDbHelper.DELETED , models, Order.class);
	            if (statusCode != HttpStatus.SC_OK) {
	            	return statusCode;
	            }
	            Log.i(TAG, "delete order size: " + models.size() + " url:" + urlwithQury);
	            for ( BaseModel order : models) {
	            	//TODO: we should use a batch call here
	            	//!!! the get deteled order is not work anymore.
		            ContentProviderUtil.delete(ctx, LeafContract.Order.CONTENT_URI, order);
	            }
	            return statusCode;
	            } catch (Exception e) {
	            Log.e(TAG, "failed processing json", e);
	        }
	        return -1;	
	}
	
	
	private Map<String, Order> getNewLocalOrders(Context ctx) {
		Map<String, Order> result = new HashMap<String, Order>();
		Cursor cursor = ctx.getContentResolver().query(LeafContract.Order.CONTENT_URI, null, LeafDbHelper.NEW_SYNC, null, null);
		if (cursor.getCount() > 0) {
			int jsonColIndex = cursor.getColumnIndex(LeafDbHelper._JSON);
			int uuidIndex = cursor.getColumnIndex(LeafDbHelper._UUID);
			while (cursor.moveToNext()) {
				String json = cursor.getString(jsonColIndex);
				String uuid = cursor.getString(uuidIndex).toUpperCase();
				Order order = new Order(json.getBytes());
				result.put(uuid, order);
			}			
		}
		
		return result;
	}
	private void mergeLocalNewOrder(Order order, Map<String, Order> newOrderMap) {
		String uuid = order.getUuid().toUpperCase();
		Order localOrder = newOrderMap.get(uuid);
		if ( localOrder != null) {	
			Log.d(TAG, "found Match");
			order.setLine_items(localOrder.getLine_items());
			order.setDiscount(localOrder.getDiscount());
			order.setId(order.getId());
			order.setSyncFlag(LeafDbHelper.MODIFIED);
		} else {
			order.setSyncFlag(LeafDbHelper.CLEAN);
		}
	}
	

	public int processSync(Context ctx, Bundle extras) {
		Log.i(TAG, "processSync");
		Cursor cursor = ctx.getContentResolver().query(LeafContract.Order.CONTENT_URI, null, LeafDbHelper.NEEDS_SYNC, null, null);
		if (cursor.getCount() > 0) {
			int jsonColIndex = cursor.getColumnIndex(LeafDbHelper._JSON);
			int syncFlagIndex = cursor.getColumnIndex(LeafDbHelper._SYNCFLAG);
			while (cursor.moveToNext()) {
				int syncFlagValue = cursor.getInt(syncFlagIndex);
				String json = cursor.getString(jsonColIndex);				
				Order order = new Order(json.getBytes());

				if (order.getId() == null) {
					Order returnedOrder = createOrder(order);
					order = returnedOrder.copySubItems(order);
					updateContentProvider(ctx, order);
				}

				if ( syncFlagValue == LeafDbHelper.MODIFIED) {			
					order.trimForRestCall();
					Order returnedOrder = updateOrder(order);
					updateContentProvider(ctx, returnedOrder);
				} else if (syncFlagValue == LeafDbHelper.DELETED ) {
					if (deleteOrder(order))
						ContentProviderUtil.delete(ctx, LeafContract.Order.CONTENT_URI, order);
				}
			}			
		}
		
		updateChangesFromServer(ctx, extras);
		return 0;
	}

	private void updateContentProvider(Context ctx, Order order) {
		if (order != null) {
			ContentProviderUtil.updateContentProvider(ctx, LeafContract.Order.CONTENT_URI, order, LeafDbHelper.CLEAN);
		}
	}
		
	private boolean deleteOrder(Order order) {
		boolean success = false;
        String url = LeafRest.LEAF_URL + Order.URLPATH + "/" + order.getId().toString();
        Request request = new Request(Method.DELETE, url, null, null);
        Response response = RestClient.getInstance().execute(request);
        int responseCode = response.getStatus();
        if (responseCode == HttpStatus.SC_OK || responseCode == HttpStatus.SC_NO_CONTENT ) {
        	Log.d(TAG, "responseCode = " + responseCode);
        	success = true;
        } else {
        	Log.d(TAG, "unexpected delete responseCode : " + responseCode);

        }
        
        return success;
	}

	private Order createOrder(Order order)  {      
        String url = LeafRest.LEAF_URL + Order.URLPATH;
        Order returnedOrder = null;
        Log.i(TAG, "create url: " + url);
        Log.i(TAG, "create request:" + order.getCreateRequest().toString());
        Request request = new Request(Method.POST, url, null, order.getCreateRequest().toString().getBytes());
        Response response = RestClient.getInstance().execute(request);
        
        int responseCode = response.getStatus();
    	String body = new String(response.getBody());
    	if (responseCode == HttpStatus.SC_CREATED) {
	    	try {
		    	JSONObject returnedJson = new JSONObject(body);
				Log.i(TAG, "createOrder result:"+ returnedJson.toString(4));
				returnedOrder = new Order(returnedJson);
			} catch (JSONException e) {
			}
    	} else {
        	Log.e(TAG, "createOrder responseCode = " + responseCode);
        	Log.e(TAG, "createOrder response:\n" + body);
    	}
    	return returnedOrder;
	}
	
	private Order updateOrder(Order order)  {        
        String url = LeafRest.LEAF_URL + Order.URLPATH + "/" + order.getId().toString();

        Order returnedOrder = null;
        
        Request request = new Request(Method.PATCH, url, null, order.getJsonObject().toString().getBytes());
        Response response = RestClient.getInstance().execute(request);
        
        int responseCode = response.getStatus();
    	String body = new String(response.getBody());
       	if( responseCode == HttpStatus.SC_CREATED) {
       		try {
				JSONObject returnedJson = new JSONObject(body);
				returnedOrder = new Order(returnedJson);
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    	Log.i(TAG, "responseCode = " + responseCode);
	    	Log.i(TAG, "PATCH response:\n" + body);
    	} else {
	    	Log.e(TAG, "updateOrder responseCode = " + responseCode);
	    	Log.e(TAG, "updateOrder response:\n" + body);
    	}
    	return returnedOrder;
	 }
}
