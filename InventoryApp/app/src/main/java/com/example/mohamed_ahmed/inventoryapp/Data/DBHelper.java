package com.example.mohamed_ahmed.inventoryapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mohamed_Ahmed on 23/10/2017.
 */
public class DBHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "Inventory_DB.dp";
    final static int DB_VERSION = 1;
    final static String CREATE_TABLE = "create table " + Contracts.InvetoryTable.TableName + "(" +
            Contracts.InvetoryTable._ID + " INTEGER PRIMARY KEY , " +
            Contracts.InvetoryTable.ProductName + " TEXT NOT NULL ," +
            Contracts.InvetoryTable.ProductPrice + " TEXT NOT NULL, " +
            Contracts.InvetoryTable.QuantityAvailable + " TEXT NOT NULL," +
            Contracts.InvetoryTable.Image + " BLOB NOT NULL ," +
            Contracts.InvetoryTable.Supplier + " TEXT NOT NULL)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.InvetoryTable.TableName);
        onCreate(db);
    }
}
