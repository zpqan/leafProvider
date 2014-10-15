package com.leaf.leafData.provider;

import android.net.Uri;


public class LeafContract {
    private  LeafContract() {}

    public static final String AUTHORITY = "com.leaf.dataprovider";
    public static final String CONTENT_TYPE  = "vnd.android.cursor.dir/vnd.leaf.leafme";
    public static final String CONTENT_ITEM_TYPE  = "vnd.android.cursor.item/vnd.leaf.leafme";

    static final String SCHEME = "content://";
    public static final String URI_PREFIX = SCHEME + AUTHORITY;
    public static final String CREATED = "timestamp";
 
   
    public static final class Order {
        private Order() {}

        public static final String TABLE_NAME = "Order_table";
        
        public static final String RESOURCE_NAME = "orders";
        
        private static final String URI_PATH = "/" + RESOURCE_NAME;
        private static final String URI_PATH_BY_ID = "/" + RESOURCE_NAME + "/#";

        public static final Uri CONTENT_URI = Uri.parse(URI_PREFIX + URI_PATH);
        public static final Uri CONTENT_URI_BY_ID = Uri.parse(URI_PREFIX + URI_PATH_BY_ID);
    }


    public static final class Printer {
        private Printer() {}

        public static final String TABLE_NAME = "Printer_table";
        
        public static final String RESOURCE_NAME = "printers";
        
        private static final String URI_PATH = "/" + RESOURCE_NAME;
        private static final String URI_PATH_BY_ID = "/" + RESOURCE_NAME + "/#";

        public static final Uri CONTENT_URI = Uri.parse(URI_PREFIX + URI_PATH);
        
        public static final Uri CONTENT_URI_BY_ID = Uri.parse(URI_PREFIX + URI_PATH_BY_ID);
    }

   

    public static final String[] ALLTABLES = new String[] {
        Order.TABLE_NAME,
        Printer.TABLE_NAME,
    };
}
