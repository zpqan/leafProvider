package com.leaf.leafData.provider;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;



public class LeafDataType {
    private final static String TAG = LeafDataType.class.getSimpleName();

    
    public enum DataTypes {
        Order,
        Printer,
  }
    
    public enum LeafPath {
        orders,
        printers,
     }

    private LeafDataType() {}

    static Map<LeafPath, DataTypes> leafPathToDataType = new HashMap<LeafPath, DataTypes>();
    static {
        leafPathToDataType.put(LeafPath.orders, DataTypes.Order);
        leafPathToDataType.put(LeafPath.printers, DataTypes.Printer);
     }

    public static DataTypes getDataType(LeafPath leafPath) {
        return leafPathToDataType.get(leafPath);
    }
    
    public static DataTypes getDataType(String typeName) {
        
        try {
            return DataTypes.valueOf(typeName);
            } catch (IllegalArgumentException e) {
            Log.d (TAG, "typeName came in as: " + typeName);
            
            if (typeName.equals(LeafContract.Printer.TABLE_NAME))
            return DataTypes.Printer;

            throw new IllegalArgumentException("bad value sent to getDataType");
        }
    }

}
