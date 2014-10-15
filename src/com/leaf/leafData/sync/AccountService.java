package com.leaf.leafData.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.leaf.leafData.BuildConfig;


public class AccountService extends Service {
    private static final String TAG = "AUTH_SVC";


    private volatile AccountMgr mgr;

    @Override
    public void onCreate() {
        super.onCreate();
        mgr = new AccountMgr(getApplicationContext());
        if (BuildConfig.DEBUG) { Log.d(TAG, "created"); }
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (BuildConfig.DEBUG) { Log.d(TAG, "bound"); }
        return mgr.getIBinder();
    }


    @Override
    public void onDestroy() {
        mgr = null;
        if (BuildConfig.DEBUG) { Log.d(TAG, "destroyed"); }
    }
}
