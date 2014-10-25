package com.leaf.client.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContentProviderOperation.Builder;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.leaf.leafData.DataModel.BaseModel;
import com.leaf.leafData.provider.LeafContract;
import com.leaf.leafData.provider.LeafDbHelper;
import com.leaf.leafData.schema.Schema;

public class ContentProviderUtil {
	private static final String TAG = ContentProviderUtil.class.getSimpleName();

	public static ContentValues modelToContentValue(BaseModel model) {
		ContentValues cv = new ContentValues();
		if ( model.getId() != null ) cv.put(LeafDbHelper._LEAF_ID, model.getId());
		if ( model.getVersion() != null ) cv.put(LeafDbHelper._VERSION, model.getVersion());
		if ( model.getUuid() != null ) cv.put(LeafDbHelper._UUID, model.getUuid());
		if ( model.getJsonObject() != null ) cv.put(LeafDbHelper._JSON, model.getJsonObject().toString());
		cv.put(LeafDbHelper._SYNCFLAG, model.getSyncflag());
		addIndedFiledValues(cv,  model);
		return cv;
	}

	private static void addIndedFiledValues(ContentValues cv, BaseModel model) {
		Schema schema = SchemaUtil.getSchema(model.getClass().getName());
		for ( String fieldName : schema.getIndexedFieldNames()) {
			JSONObject jsonObj = model.getJsonObject();
			if ( schema.getFeildType(fieldName).equals(Schema.BOOLEAN_TYPE)) {
				Boolean value = DataModelUtils.getBooleanFromJson(jsonObj, fieldName);
				if ( value != null) {
					if ( value ) cv.put(fieldName, 1);
					else cv.put(fieldName, 0);
				}
			} else if (schema.getFeildType(fieldName).equals(Schema.INTEGER_TYPE)) {
				Integer value = DataModelUtils.getIntegerFromJson(jsonObj, fieldName);
				if ( value != null) {
					cv.put(fieldName, value);
				}
			} else if (schema.getFeildType(fieldName).equals(Schema.STRING_TYPE)) {
				String value = DataModelUtils.getStringFromJson(jsonObj, fieldName);
				if ( value != null) {
					cv.put(fieldName, value);
				}
			}
		}
	}
	
	public static void updateContentProvider(Context ctx, Uri uri, List<BaseModel> models, int syncFlagValue) {
		ContentResolver cr = ctx.getContentResolver();   
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		for ( BaseModel model : models) {
			prepareOp(uri, syncFlagValue, ops, model);
		}
		try {
			cr.applyBatch(LeafContract.AUTHORITY, ops);
			Log.i(TAG, "updateContent successful");
		} catch (Exception e) {
			Log.e(TAG, "updateContentProvider failed", e);
		}
	}
	

	public static void updateContentProvider(Context ctx, Uri uri, List<BaseModel> models) {
	    	ContentResolver cr = ctx.getContentResolver();   
	       	ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
	    	for ( BaseModel model : models) {
	    		Builder builder = null;
	    		if ( model.getSyncflag() == LeafDbHelper.CLEAN) {
	    			builder = ContentProviderOperation.newUpdate(uri);
	    			builder.withSelection(LeafDbHelper._LEAF_ID + "=?", new String[]{model.getId().toString()});
	    		} else if (model.getSyncflag() == LeafDbHelper.NEW) {
	    			builder = ContentProviderOperation.newUpdate(uri);
	    			builder.withSelection(LeafDbHelper._UUID + "=? COLLATE NOCASE", new String[]{model.getUuid()});
	    		} else if ( model.getSyncflag() == LeafDbHelper.DELETED) {
	    			builder = ContentProviderOperation.newDelete(uri);
	    			builder.withSelection(LeafDbHelper._LEAF_ID + "=?", new String[]{model.getId().toString()});
	    		}
	    		
	    		if (model.getSyncflag() != LeafDbHelper.DELETED ) {
		    		ContentValues cvs = ContentProviderUtil.modelToContentValue(model);
		    		ops.add(builder.withValues(cvs).build());
	    		} else {
	    			ops.add(builder.build());
	    		}
	    	}
	    	try {
	    		cr.applyBatch(LeafContract.AUTHORITY, ops);
	    		Log.i(TAG, "updateContent successful");
	    	} catch (Exception e) {
	    		Log.e(TAG, "updateContentProvider failed", e);
	    	}
	    	
	     }

	public static void updateContentProvider(Context ctx, Uri uri, BaseModel model, int syncFlagValue) {
		ContentResolver cr = ctx.getContentResolver();   
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		prepareOp(uri, syncFlagValue, ops, model);
		try {
			cr.applyBatch(LeafContract.AUTHORITY, ops);
			Log.i(TAG, "updateContent successful");
		} catch (Exception e) {
			Log.e(TAG, "updateContentProvider failed", e);
		}
	}


	private static void prepareOp(Uri uri, int syncFlagValue,
			ArrayList<ContentProviderOperation> ops, BaseModel model) {
		ContentValues cvs = ContentProviderUtil.modelToContentValue(model);
		cvs.put(LeafDbHelper._SYNCFLAG, syncFlagValue);
		Builder builder = ContentProviderOperation.newUpdate(uri);
		addWhere(builder,  model);
		ops.add(builder.withValues(cvs).build());
	}

	public static void delete(Context ctx, Uri uri, BaseModel model) {
		Builder builder = ContentProviderOperation.newDelete(uri);
		addWhere(builder, model);
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ContentProviderOperation op = builder.build();
		ops.add(op);
		try {
			ctx.getContentResolver().applyBatch(LeafContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			e.printStackTrace();
		}
	}


	private static void addWhere(Builder builder, BaseModel model) {
		String where1 = "";
		String where2 = "";
		if (model.getId() != null)
			where1 = LeafDbHelper._LEAF_ID + "=?" ;
		if (model.getUuid() != null)
			where2 = LeafDbHelper._UUID + "=?";
		String where = null;
		String[] args = null;
		if (!TextUtils.isEmpty(where1) && !TextUtils.isEmpty(where2)) {
			where = where1 + " OR " + where2;
			args = new String[] {model.getId().toString(), model.getUuid()};
		} else if (!TextUtils.isEmpty(where1)) {
			where = where1;
			args = new String[] {model.getId().toString()};
		} else if (!TextUtils.isEmpty(where2)) {
			where = where2;
			args = new String[] {model.getUuid()};
		} else where = null;
			
		builder.withSelection(where, args);
	}

}
