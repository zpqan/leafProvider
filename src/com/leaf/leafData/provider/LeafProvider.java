package com.leaf.leafData.provider;

import java.io.File;
import java.io.FileNotFoundException;

import com.leaf.client.util.SchemaUtil;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class LeafProvider extends ContentProvider {
    private static final String TAG = LeafProvider.class.getSimpleName();


	private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        for (int i = 0; i < LeafContract.ALLTABLES.length; i++) {
            uriMatcher.addURI(LeafContract.AUTHORITY, LeafContract.ALLTABLES[i], i*2);
            uriMatcher.addURI(LeafContract.AUTHORITY, LeafContract.ALLTABLES[i] + "/#", i*2 + 1);
        }
    }

	private LeafDbHelper dbHelper;

	@Override
	public boolean onCreate() {
		this.dbHelper = new LeafDbHelper(getContext());
		return true;
	}

    @Override
    public String getType(Uri uri) {
		if (uri == null) {
			throw new IllegalArgumentException("getType called with null Uri ");	
		}
		
		String tableName = getTableName(uri);
		return LeafContract.CONTENT_TYPE + "." + tableName;

    }

	@Override
	public int delete(Uri uri, String whereClause, String[] whereValues) {
		//Log.i(TAG, " delete uri: " + uri +  "where " + whereClause + "  " + whereValues[0]);
		if (uri == null)
			throw new IllegalArgumentException("delete called with null Uri ");
		
		String tableName = getTableName(uri);
		
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        int deletedRowsCount;
		try {
	        db.beginTransaction();
		    deletedRowsCount = db.delete(tableName, whereClause, whereValues);
	        db.setTransactionSuccessful();
		}
		finally { db.endTransaction(); }

		getContext().getContentResolver().notifyChange(uri, null);
		return deletedRowsCount;
	}

	private String getTableName(Uri uri) {
		if (uri == null) {
			throw new IllegalArgumentException("called with null Uri ");	
		}
		String resourceName = getPathName(uri);
		String tableName = SchemaUtil.getTableName(resourceName);
		return tableName;
	}
	
	private String getPathName(Uri uri) {
		return uri.getPathSegments().get(0);
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (uri == null)
			throw new IllegalArgumentException("insert called with null Uri ");
		String tableName = getTableName(uri);
		
		if (values == null) {
			throw new SQLException("ContentValues arg for .insert() is null, cannot insert row.");
		}

		try {
			long newRowId = this.dbHelper.getWritableDatabase().insert(tableName, null, values);
			Log.d(TAG, "newRowId = " + newRowId);

			// if rowID is -1, it means the insert failed
			if (newRowId > 0) {
				Uri itemUri = ContentUris.withAppendedId(uri, newRowId);
				getContext().getContentResolver().notifyChange(itemUri, null);
				return itemUri;
			}
		} catch (SQLiteConstraintException e) {
			Log.d(TAG, e.getMessage());
		}
 		
        // Insert failed: halt and catch fire.
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public Cursor query(
	        Uri uri,
	        String[] proj,
	        String whereClause,
	        String[] whereValues,
	        String sortOrder)
	{
		//Log.i(TAG, " uri: " + uri + " whereClause: " + whereClause + " whereValues:" + whereValues);
		if (uri == null)
			throw new IllegalArgumentException("query called with null Uri ");
		String tableName = getTableName(uri);

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(tableName);

		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		Cursor cursor = qb.query(db, proj, whereClause, whereValues, null, null, sortOrder);

		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues updateValues, String whereClause, String[] whereValues) {
		if (uri == null)
			throw new IllegalArgumentException("update called with null Uri ");
		String tableName = getTableName(uri);
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
	    int updatedRowsCount;
	    updatedRowsCount = db.update(tableName, updateValues, whereClause, whereValues);
	    boolean inserted = false;
	    if (updatedRowsCount == 0 ) {
	    	inserted = insert(tableName, null, updateValues, db) != -1;
	    }
//	    try {
//	        db.beginTransaction();
//            updatedRowsCount
//                = db.update(tableName, updateValues, whereClause, whereValues);
//            db.setTransactionSuccessful();
//	    }
//	    finally {
//	        db.endTransaction();
//	    }

	    if (updatedRowsCount > 0 || inserted) {
	        getContext().getContentResolver().notifyChange(uri, null);
	    }

	    return updatedRowsCount;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		if (uri == null)
			throw new IllegalArgumentException("bulkInsert called with null Uri ");
		String tableName = getTableName(uri);
		
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		Log.d(TAG, "bulkInsert attempt to insert " + values.length + " values");
		int insertedCount = 0;
		try {
	        db.beginTransaction();
			for (ContentValues cv : values) {
				long newRowId = insert(tableName, uri, cv, db);
				if (newRowId > -1) insertedCount++;
			}
			db.setTransactionSuccessful();
		}
		finally {
			db.endTransaction();
		}

		if (0 < insertedCount) { getContext().getContentResolver().notifyChange(uri, null); }

		Log.d(TAG, insertedCount + " values successfully inserted");
		return insertedCount;
	}

	private long insert(String tableName, Uri uri, ContentValues values, SQLiteDatabase writableDb) {
		if (values == null) {
			throw new SQLException("ContentValues arg for .insert() is null, cannot insert row.");
		}
		long newRowId = -1;
		try {
			newRowId = writableDb.insert(tableName, null, values);
			//Log.d(TAG, "insert newRowId = " + newRowId);
		} catch (SQLiteConstraintException e) {

		} 

		return newRowId;
	}
	
	
	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		return ParcelFileDescriptor.open(new File(
            this.getContext().getFilesDir(),
            uri.getPath()),
            ParcelFileDescriptor.MODE_READ_ONLY);
	}

//    private String appendPkConstraint(String whereClause, String id) {
//        String where = android.provider.BaseColumns._ID + " = " + id;
//        whereClause = (TextUtils.isEmpty(whereClause))
//            ? where
//            : "(" + where + ") AND (" + whereClause + ")";
//
//        return whereClause;
//    }
}
