package com.leaf.leafData.sync;

import java.io.IOException;

import android.R;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.leaf.leafData.ClientApp;
import com.leaf.leafData.processor.OrderProcessor;
import com.leaf.leafData.processor.PrinterProcessor;


public class SyncAdapter extends AbstractThreadedSyncAdapter {
	private static final String TAG = SyncAdapter.class.getSimpleName();
    private static final String SYNC_MARKER_KEY = "com.leaf.leafData.SyncMarker_Key";
    
    private final ClientApp mctx;
    private AccountManager mAccountManager;
    public SyncAdapter(ClientApp ctx, boolean autoInitialize) {
        super(ctx, autoInitialize);
        this.mctx = ctx;
    }

    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {
    	mAccountManager = AccountManager.get(mctx);

      Exception e = null;
      String token = null;
      String tt = "AUTH_UUID";
      try { token = mAccountManager.blockingGetAuthToken(account, tt, false); }
      catch (OperationCanceledException oce) { e = oce; }
      catch (AuthenticatorException ae) { e = ae; }
      catch (IOException ioe) { e = ioe; }

      if (null == token) {
          Log.e(TAG, "auth failed: " + AccountMgr.acctStr(account) + "#" + tt, e);
          return;
      }
    	
    	
    	long currentTime = System.currentTimeMillis();
    	long lastUpdateTime = getServerSyncMarker(account);
        Log.i(TAG, "calling sync");
        OrderProcessor orderProcessor = new OrderProcessor();
       // orderProcessor.processSync(ctx, extras);
        PrinterProcessor printerProcessor = new PrinterProcessor();
        printerProcessor.processSync(mctx, extras);
        setServerSyncMarker(account, currentTime);
        ContentResolver.addPeriodicSync(account, authority, extras, 120);
        Log.i(TAG, "sync done");
//        
//
//
//        new RESTService(ctxt).sync(account, token);
//
//        // force re-validation
//        mgr.invalidateAuthToken(account.type, token);

       
    }
    
    /**
     * This helper function fetches the last known high-water-mark
     * we received from the server - or 0 if we've never synced.
     *
     * @param account the account we're syncing
     * @return the change high-water-mark
     */
    private long getServerSyncMarker(Account account) {
        String markerString = mAccountManager.getUserData(account, SYNC_MARKER_KEY);
        if (!TextUtils.isEmpty(markerString)) {
            return Long.parseLong(markerString);
        }
        return 0;
    }

    /**
     * Save off the high-water-mark we receive back from the server.
     *
     * @param account The account we're syncing
     * @param marker  The high-water-mark we want to save.
     */
    private void setServerSyncMarker(Account account, long marker) {
        mAccountManager.setUserData(account, SYNC_MARKER_KEY, Long.toString(marker));
    }
}
