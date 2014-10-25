package com.leaf.leafData.provider;

import com.leaf.client.util.SchemaUtil;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;



/**
* This creates, updates, and opens the database.  Opening is handled by the superclass, we handle
* the create & upgrade steps
*/
public class LeafDbHelper extends SQLiteOpenHelper {
    public final String TAG = getClass().getSimpleName();
    
    public static final int CLEAN = 0;
    public static final int NEW = 1 << 4;
    public static final int MODIFIED = 1 << 3;
    public static final int DELETED =  1 << 1;

    public static final String[] CLEAN_ARGLIST = { Integer.toString(CLEAN) };
    public static final String[] MODIFIED_ARGLIST = { Integer.toString(MODIFIED) };
    public static final String[] DELETED_ARGLIST = { Integer.toString(DELETED) };
    public static final String[] NEW_ARGLIST = { Integer.toString(NEW) };
    
    public static final String _SYNCFLAG = "_syncFLAG";
    public static final String _UUID = "_UUID";
    public static final String _VERSION = "_VERSION";
    public static final String _MODIFICATION_TIME = "_MODIFICATION_TIME";
    public static final String _JSON = "_JSON";
    public static final String _LEAF_ID = "_LEAF_ID";
   	
    
    
    public static final String NEEDS_SYNC = _SYNCFLAG + "!=" + CLEAN;
    public static final String NEW_SYNC =  _SYNCFLAG + "==" + NEW;
       
    // Name of the database file
    private static final String DATABASE_NAME = "leafclient.db";
    private static final int DATABASE_VERSION = 1;

    public LeafDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private String getCreateTableSql(String tableName) {
    	return 
    	"CREATE TABLE " + tableName + " ("
    	 		+ BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT "
    	 		+ ", " + LeafDbHelper._UUID + " STRING  COLLATE NOCASE "
        	    + ", " + LeafDbHelper._SYNCFLAG + " INTEGER "
         	    + ", " + LeafDbHelper._VERSION + " INTEGER " 
        	    + ", " + LeafDbHelper._MODIFICATION_TIME + " STRING "
        	    + ", " + LeafDbHelper._LEAF_ID + " INTEGER "
        	    + ", " + LeafDbHelper._JSON + " STRING "
        	    + ", UNIQUE(" + LeafDbHelper._LEAF_ID + ") ON CONFLICT REPLACE "
        	    + ");";	
    }

    private String getDropTableSql(String tableName) {
    	return "DROP TABLE IF EXISTS " + tableName;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	
        Log.d(TAG, "creating database tables");
        db.execSQL(SchemaUtil.getSqlCreateTable(LeafContract.Order.RESOURCE_NAME));
        db.execSQL(SchemaUtil.getSqlCreateTable(LeafContract.Printer.RESOURCE_NAME));
       
//        db.execSQL(Z2DM_CatalogItem.DB_CREATE);
//        db.execSQL(Z2DM_CatalogItemModifier.DB_CREATE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateDb(db);
    }

    private void recreateTable(SQLiteDatabase db, String tableName) {
    	if ( tableName.equals(LeafContract.Order.TABLE_NAME)) {
    		db.execSQL(getDropTableSql(SchemaUtil.getTableName(LeafContract.Order.RESOURCE_NAME)));
    		db.execSQL(SchemaUtil.getSqlCreateTable(LeafContract.Order.RESOURCE_NAME));
    	} else if ( tableName.equals(LeafContract.Printer.TABLE_NAME)) {
    		db.execSQL(getDropTableSql(SchemaUtil.getTableName(LeafContract.Printer.RESOURCE_NAME)));
    		db.execSQL(SchemaUtil.getSqlCreateTable(LeafContract.Printer.RESOURCE_NAME));
    	} else {
    		db.execSQL(getDropTableSql(tableName));
    		db.execSQL(getCreateTableSql(tableName));	
    	}
  }
   
   
    
    public void recreateDb(SQLiteDatabase db) {
        Log.d(TAG, "reset tables");
        
        recreateTable(db,LeafContract.Order.RESOURCE_NAME);
        recreateTable(db,LeafContract.Printer.RESOURCE_NAME);
        recreateTable(db,LeafContract.CatalogItem.TABLE_NAME);
        recreateTable(db,LeafContract.CatalogItemModifier.TABLE_NAME);
        onCreate(db);
    }
    
    
}
