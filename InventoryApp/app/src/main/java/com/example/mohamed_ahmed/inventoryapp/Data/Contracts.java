package com.example.mohamed_ahmed.inventoryapp.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contracts {
    public static final String CONTENT_AUTHORITY = "com.example.mohamed_ahmed";
    public static final String PATH = InvetoryTable.TableName;
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);

    public class InvetoryTable implements BaseColumns {
        public final static String TableName = "InventoryBD";
        public final static String ProductName = "Name";
        public final static String ProductPrice = "Price";
        public final static String QuantityAvailable = "Available";
        public final static String Supplier = "Supplier";
        public final static String Image = "ProductImage";
    }
}
