package com.leaf.leafData.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.leaf.leafData.util.SchemaUtil;


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
    private static final String DATABASE_NAME = "leafData.db";
    private static final int DATABASE_VERSION = 1;

    public LeafDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    } 

    private String getDropTableSql(String tableName) {
    	return "DROP TABLE IF EXISTS " + tableName;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "creating database tables");
        db.execSQL(SchemaUtil.getSqlCreateTable(LeafContract.Order.TABLE_NAME));
        db.execSQL(SchemaUtil.getSqlCreateTable(LeafContract.Printer.TABLE_NAME));
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateDb(db);
    }

    public void recreateDb(SQLiteDatabase db) {
        Log.d(TAG, "drop existing tables");
        db.execSQL(getDropTableSql(LeafContract.Order.TABLE_NAME));
        db.execSQL(getDropTableSql(LeafContract.Printer.TABLE_NAME));
        onCreate(db);
    }
    
}
