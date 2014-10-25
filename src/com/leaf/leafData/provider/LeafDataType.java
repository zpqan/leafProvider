package com.leaf.leafData.provider;

import java.util.HashMap;
import android.util.Log;

public class LeafDataType {
    private final static String TAG = LeafDataType.class.getSimpleName();

    
    public enum DataTypes {
        CatalogItem,
        CatalogItemModifier,
        Order,
        Printer
    }
    
    public enum LeafPath {
        catalog_items,
        catalog_item_modifiers,
        orders,
        printers,
    }

    private LeafDataType() {}

    static HashMap<LeafPath, DataTypes> leafPathToDataType;
    static {
        leafPathToDataType = new HashMap<LeafPath, DataTypes>();
        leafPathToDataType.put(LeafPath.catalog_item_modifiers, DataTypes.CatalogItemModifier);
        leafPathToDataType.put(LeafPath.catalog_items, DataTypes.CatalogItem);
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
            if (typeName.equals(LeafContract.CatalogItem.TABLE_NAME))
             return DataTypes.CatalogItem;
            if (typeName.equals(LeafContract.CatalogItemModifier.TABLE_NAME))
            return DataTypes.CatalogItemModifier;
             if (typeName.equals(LeafContract.Order.RESOURCE_NAME))
            return DataTypes.Order;
            if (typeName.equals(LeafContract.Printer.RESOURCE_NAME))
             return DataTypes.Printer;
           throw new IllegalArgumentException("bad value sent to getDataType");
        }
    }
  
}
