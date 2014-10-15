package com.leaf.leafData;


import android.app.Application;
import android.util.Log;


public class ClientApp extends Application{
	private static final String TAG = ClientApp.class.getSimpleName();

	public static final String SERVICE_CALL_TYPE = "SERVICE_CALL_TYPE";
	public static final String REST_SERVICE_CALL_TYPE = "REST_CALLL_TYPE";
	public static final String SYNC_SERVICE_CALL_TYPE = "SYNC_CALLL_TYPE";
	public static final String ACTION_REQUEST_RESULT = "REST_RQUEST";
	public static final String REQUEST_ID = "REQUEST_ID";
	public static final String RESULT_CODE = "RESULT_CODE";
	public static final String REST_METHOD = "REST_METHOD";
	public static final String REST_PARAMS = "REST_PARAMS";
	public static final String SERVICE_CALLBACK = "com.leaf.leafclient.rest.service.SERVICE_CALLBACK";
	public static final String SERVICE_REQUEST = "com.leaf.leafclient.restfulandroid.service.SERVICE.REQUEST";
	public static final String SERVICE_PROCESSOR = "com.leaf.leafclient.rest.service.PROCESSOR";
	
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) { Log.d(TAG, "Application up!"); }
   	
    }
	
}
