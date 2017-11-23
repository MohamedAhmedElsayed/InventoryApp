package com.example.mohamed_ahmed.inventoryapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mohamed_ahmed.inventoryapp.R;

/**
 * Created by Mohamed_Ahmed on 23/10/2017.
 */
public class Cursor_Adapter extends android.widget.CursorAdapter {
    Context con;

    public Cursor_Adapter(Context context, Cursor c) {
        super(context, c, 0);
        con = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.raw_view, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        TextView Name = (TextView) view.findViewById(R.id.ProductName);
        final TextView Quantity = (TextView) view.findViewById(R.id.ProductQuantity);
        TextView Price = (TextView) view.findViewById(R.id.ProductPrice);
        TextView PID = (TextView) view.findViewById(R.id.PID);
        PID.setText(cursor.getString(cursor.getColumnIndex(Contracts.InvetoryTable._ID)));
        Price.setText("Product Name " + cursor.getString(cursor.getColumnIndex(Contracts.InvetoryTable.ProductPrice)));
        Name.setText("Product Price " + cursor.getString(cursor.getColumnIndex(Contracts.InvetoryTable.ProductName)));
        Quantity.setText(cursor.getString(cursor.getColumnIndex(Contracts.InvetoryTable.QuantityAvailable)));
        TextView but = (TextView) view.findViewById(R.id.But_Buy);
        final int index = cursor.getInt(cursor.getColumnIndex(Contracts.InvetoryTable._ID));
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtID = (TextView) view.findViewById(R.id.PID);
                TextView txtQu = (TextView) view.findViewById(R.id.ProductQuantity);
                int q = Integer.parseInt(txtQu.getText().toString());
                int abc = Integer.parseInt(txtID.getText().toString());
                cursor.moveToPosition(abc);
                if (q > 0) {
                    q--;
                    String where = Contracts.InvetoryTable._ID + " = ?";
                    String[] Args = {abc + ""};
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contracts.InvetoryTable.QuantityAvailable, q);
                    int i = context.getContentResolver().update(Contracts.CONTENT_URI, contentValues, where, Args);
                }
            }
        });
    }
}

