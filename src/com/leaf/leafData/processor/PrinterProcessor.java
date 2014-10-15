package com.leaf.leafData.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.leaf.client.util.ContentProviderUtil;
import com.leaf.client.util.SyncUtil;
import com.leaf.leafData.ClientApp;
import com.leaf.leafData.provider.LeafContract;
import com.leaf.leafData.provider.LeafDbHelper;
import com.leaf.leafData.rest.LeafRest;
import com.leaf.leafData.rest.Request;
import com.leaf.leafData.rest.Request.Method;
import com.leaf.leafData.rest.Response;
import com.leaf.leafData.rest.RestClient;
import com.leaf.leafclient.dataModel.BaseModel;
import com.leaf.leafclient.dataModel.Printer;


public class PrinterProcessor {
	private final String TAG = PrinterProcessor.class.getSimpleName();
	private static final String urlString = LeafRest.LEAF_URL + Printer.URLPATH;
	
	public PrinterProcessor(){
		
	}
	
	public int processRestCall(Context ctx, Bundle extras) {
	      try { 
	    	  	String urlwithQury = createGetAllUrl(extras);
	    	  	List<BaseModel> models = new ArrayList<BaseModel>();
	            int statusCode = SyncUtil.getChangedFromServer(urlwithQury, LeafDbHelper.CLEAN , models, Printer.class);
	            if (statusCode != HttpStatus.SC_OK) {
	            	Log.e(TAG, "rest call failed with error code: " + statusCode);
	            	return statusCode;
	            }
	            Log.i(TAG, "modified printers size: " + models.size());
	            ContentProviderUtil.updateContentProvider(ctx, LeafContract.Printer.CONTENT_URI, models);
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
        return urlTemp + "per_page=200";

	}
	private String createGetAllDeletedUrl(Bundle extras) {
		String params = "";
        if (extras != null) {
            params = extras.getString(ClientApp.REST_PARAMS);
        }      
        String urlTemp = (params == null || params.length()==0) ? urlString + "?" : urlString + params + "&";
        return urlTemp + "per_page=200&deleted=true";

	}
	
	public int processSync(Context ctx, Bundle extras) {
		Log.i(TAG, "processSync");
		Cursor cursor = ctx.getContentResolver().query(LeafContract.Printer.CONTENT_URI, null, LeafDbHelper.NEEDS_SYNC, null, null);
		if (cursor.getCount() > 0) {
			int jsonColIndex = cursor.getColumnIndex(LeafDbHelper._JSON);
			int syncFlagIndex = cursor.getColumnIndex(LeafDbHelper._SYNCFLAG);
			while (cursor.moveToNext()) {
				String json = cursor.getString(jsonColIndex);				
				Printer printer = new Printer(json.getBytes());
				int syncFlagValue = cursor.getInt(syncFlagIndex);
				if ( syncFlagValue == LeafDbHelper.MODIFIED) {			
					updatePrinter(printer);
				} else if (syncFlagValue == LeafDbHelper.DELETED ) {
					deletePrinter(printer);
				} else if (syncFlagValue == LeafDbHelper.NEW ) {
					createPrinter(printer);
				}
			}			
		}
		updateChangesFromServer(ctx, extras);
		return 0;
	}
	private int updateChangesFromServer(Context ctx, Bundle extras ) {

    	  	String urlwithQury = createGetAllUrl(extras);
    	  	List<BaseModel> models = new ArrayList<BaseModel>();
    	  	
            int statusCode = SyncUtil.getChangedFromServer(urlwithQury, LeafDbHelper.CLEAN , models, Printer.class);
            if (statusCode != HttpStatus.SC_OK) {
            	return statusCode;
            }
            //ContentProviderUtil.updateContentProvider(ctx, LeafContract.Printer.CONTENT_URI, models);
            //models.clear();
            /** we are get all deleted records from the server and then delete them **/
            urlwithQury = createGetAllDeletedUrl(extras);
            statusCode = SyncUtil.getChangedFromServer(urlwithQury, LeafDbHelper.DELETED , models, Printer.class);
            if (statusCode != HttpStatus.SC_OK) {
            	return statusCode;
            }
            Log.i(TAG, "delete printer size: " + models.size() + " url:" + urlwithQury);
//            for ( BaseModel printer : models) {
//            	//!!! the get deteled order is not work anymore.
//            	//TODO: we should use a batch call here
//	           // ContentProviderUtil.delete(ctx, LeafContract.Printer.CONTENT_URI, printer);
//            } 
            ContentProviderUtil.updateContentProvider(ctx, LeafContract.Printer.CONTENT_URI, models);
	        return statusCode;	
	}
	
 
   
	private String createPrinter(Printer printer)  { 
	    String url = LeafRest.LEAF_URL + Printer.URLPATH;
        String returnedJson = null;
        Log.i(TAG, "create url: " + url);
        Request request = new Request(Method.POST, url, null, printer.getJsonObject().toString().getBytes());
        Response response = RestClient.getInstance().execute(request);
        
        int responseCode = response.getStatus();
    	String body = new String(response.getBody());
    	if (responseCode == HttpStatus.SC_CREATED) {
	    	returnedJson = body;
    	} else {
        	Log.e(TAG, "createPrinter responseCode = " + responseCode);
        	Log.e(TAG, "createPrinter response:\n" + body);
    	}
    	return returnedJson;
	}
	
	private String updatePrinter(Printer printer)  {        
        String url = LeafRest.LEAF_URL + Printer.URLPATH + "/" + printer.getId().toString();

        String returnedJson = null;
        
        Request request = new Request(Method.PATCH, url, null, printer.getJsonObject().toString().getBytes());
        Response response = RestClient.getInstance().execute(request);
        
        int responseCode = response.getStatus();
    	String body = new String(response.getBody());
       	if( responseCode == HttpStatus.SC_CREATED) {
       		returnedJson = body;
	    	Log.i(TAG, "responseCode = " + responseCode);
	    	Log.i(TAG, "PATCH response:\n" + body);
    	} else {
	    	Log.e(TAG, "updatePrinter responseCode = " + responseCode);
	    	Log.e(TAG, "updatePriner response:\n" + body);
    	}
    	return returnedJson;
	 }
	
	private void deletePrinter(Printer printer) {
        String url = LeafRest.LEAF_URL + Printer.URLPATH + "/" + printer.getId().toString();
        Request request = new Request(Method.DELETE, url, null, null);
        Response response = RestClient.getInstance().execute(request);
        int responseCode = response.getStatus();
        if (responseCode != HttpStatus.SC_OK || responseCode != HttpStatus.SC_NO_CONTENT ) {
        	Log.d(TAG, "responseCode = " + responseCode);
        }
	}

}
