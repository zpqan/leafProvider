package com.leaf.leafData.util;

import java.util.HashMap;
import java.util.Map;

import android.provider.BaseColumns;
import android.util.Log;

import com.leaf.leafData.dataModel.BaseModel;
import com.leaf.leafData.dataModel.Order;
import com.leaf.leafData.dataModel.Printer;
import com.leaf.leafData.provider.LeafContract;
import com.leaf.leafData.provider.LeafDbHelper;
import com.leaf.leafData.schema.OrderlSchema;
import com.leaf.leafData.schema.PrinterSchema;
import com.leaf.leafData.schema.Schema;


public class SchemaUtil {
	private static final String TAG = SchemaUtil.class.getSimpleName();
	private static Map<String, Schema> typeSchemaMap = new HashMap<String,Schema>();
	private static Map<String, String> sqlTypeMap = new HashMap<String,String>();

	private static String sqlCommonHeader =  " ("
		+ BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT "
		+ ", " + LeafDbHelper._UUID + " STRING  COLLATE NOCASE "
		+ ", " + LeafDbHelper._SYNCFLAG + " INTEGER "
	    + ", " + LeafDbHelper._VERSION + " INTEGER " 
	    + ", " + LeafDbHelper._MODIFICATION_TIME + " STRING "
	    + ", " + LeafDbHelper._LEAF_ID + " INTEGER "
	    + ", " + LeafDbHelper._JSON + " STRING ";
	
	private static String sqlCommonTail = ", UNIQUE(" + LeafDbHelper._LEAF_ID + ") ON CONFLICT REPLACE "
    	    + ");";	
			
	static {	
		typeSchemaMap.put(LeafContract.Order.TABLE_NAME, new OrderlSchema());
		typeSchemaMap.put(LeafContract.Printer.TABLE_NAME, new PrinterSchema());
		typeSchemaMap.put(Order.class.getName(), new OrderlSchema());
		typeSchemaMap.put(Printer.class.getName(), new PrinterSchema());
			
		sqlTypeMap.put(Schema.STRING_TYPE, "STRING");
		sqlTypeMap.put(Schema.INTEGER_TYPE, "INTEGER");
		sqlTypeMap.put(Schema.BOOLEAN_TYPE, "INTEGER");
	}
	
	public static String getSqlCreateTable(String tableName) {
		String sql = "CREATE TABLE " + tableName +  sqlCommonHeader;
		sql += getSqlForIndexFields(tableName);
		sql += sqlCommonTail;
		Log.i(TAG, " sql: " + sql);		
		return sql;
	}
	
	public static Schema getSchema(String name) {
		Schema schema = typeSchemaMap.get(name);
		if ( schema == null) {
			throw new IllegalArgumentException(" schema name: " + name + " not found");
		}
		return schema;					
	}
	
	
	private static String getSqlForIndexFields(String tableName) {
		Schema schema = getSchema(tableName);
		String[] indexedFieldNames = schema.getIndexedFieldNames();
		StringBuffer strBuf = new StringBuffer();
		for ( String fieldName : indexedFieldNames) {
			if (fieldName != null && !fieldName.isEmpty() ) {
				String dataType = schema.getFeildType(fieldName);
				strBuf.append(", " + fieldName + " " + getSqlDataType(dataType));
			}
		}
		return strBuf.toString();
	}
	
	private static String getSqlDataType(String dataType) {
		String dataTypeUpcase = dataType.toUpperCase();
		String sqlType = sqlTypeMap.get(dataTypeUpcase);
		if ( sqlType == null) {
			throw new IllegalArgumentException("no sql data Type to match " + dataType);
		}
		return sqlType;
	}
	
}
