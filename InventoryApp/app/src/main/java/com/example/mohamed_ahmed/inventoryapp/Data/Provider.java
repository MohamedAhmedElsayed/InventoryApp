package com.example.mohamed_ahmed.inventoryapp.Data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Mohamed_Ahmed on 23/10/2017.
 */
public class Provider extends ContentProvider {
    public static final String LOG_TAG = Provider.class.getSimpleName();
    final static int Products = 1;
    final static int PrductID = 2;
    final static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(Contracts.CONTENT_AUTHORITY, Contracts.PATH, Products);
        matcher.addURI(Contracts.CONTENT_AUTHORITY, Contracts.PATH + "/#", PrductID);
    }

    DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = matcher.match(uri);
        switch (match) {
            case Products:
                cursor = db.query(Contracts.InvetoryTable.TableName, strings, null, null, null, null, null);
                break;
            case PrductID:
                String selection = Contracts.InvetoryTable._ID + "=?";
                String[] selArg = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(Contracts.InvetoryTable.TableName, null, selection, selArg, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("Cant Match URI On Query " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = matcher.match(uri);
        switch (match) {
            case Products:
                return insertRaw(uri, contentValues);
            default:
                throw new IllegalArgumentException("Cant Insert No Match Uri For " + uri);
        }
    }

    private Uri insertRaw(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id = database.insert(Contracts.InvetoryTable.TableName, null, contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int i = database.delete(Contracts.InvetoryTable.TableName, s, strings);
        getContext().getContentResolver().notifyChange(uri, null);
        if (i > 0) {
            return i;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int i = database.update(Contracts.InvetoryTable.TableName, contentValues, s, strings);
        Log.e(getClass().getSimpleName(), "Effected " + i + " value " + contentValues.get(Contracts.InvetoryTable.QuantityAvailable)
                + " where " + s + " args " + strings[0]);
        getContext().getContentResolver().notifyChange(uri, null);
        return i;
    }
}
