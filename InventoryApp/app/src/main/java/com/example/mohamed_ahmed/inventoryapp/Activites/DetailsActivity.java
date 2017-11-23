package com.example.mohamed_ahmed.inventoryapp.Activites;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed_ahmed.inventoryapp.Data.Contracts;
import com.example.mohamed_ahmed.inventoryapp.Data.Utils;
import com.example.mohamed_ahmed.inventoryapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.design.widget.Snackbar.make;

public class DetailsActivity extends AppCompatActivity {
    public String IncreaseOrDecrease;
    int CurrentQuantity;
    @BindView(R.id.Product_Name)
    TextView Pname;
    @BindView(R.id.Product_Price)
    TextView Pprice;
    @BindView(R.id.Product_Quantity)
    TextView Pquantity;
    @BindView(R.id.Product_Supplier)
    TextView Psupplier;
    @BindView(R.id.Product_Image)
    ImageView Pimage;
    @BindView(R.id.DeleteProduct)
    Button DeleteBut;
    @BindView(R.id.OrderBut)
    Button OrderBut;
    @BindView(R.id.IncreadeBut)
    Button increaseBut;
    @BindView(R.id.DecreaseBut)
    Button decreaseBut;
    Cursor c;
    private Uri uri;
    private int position;
    private CoordinatorLayout coordinatorLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .activity_details);
        ButterKnife.bind(this);
        Bundle b = getIntent().getExtras();
        position = Integer.parseInt(b.getString("RawPosition"));
        uri = ContentUris.withAppendedId(Contracts.CONTENT_URI, position);
        ShowData();
        DeleteBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder1.setMessage("Delete Product Ya M3lem ?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String where = Contracts.InvetoryTable._ID + "=?";
                                String[] Args = {position + ""};
                                int i = getContentResolver().delete(uri, where, Args);
                                if (i > 0) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            finish();
                                        }
                                    }, 2000);
                                    Snackbar snackbar =
                                            make(coordinatorLayout, "Deleted", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    dialog.cancel();
                                }
                            }
                        });
                builder1.setNegativeButton(
                        "Undo",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Snackbar snackbar =
                                        make(coordinatorLayout, "Not Deleted", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        OrderBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = c.getString(c.getColumnIndex(Contracts.InvetoryTable.Supplier));
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", Email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New Order");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        increaseBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IncreaseOrDecrease = "Increased";
                ShowDialog("Increase by ?");
            }
        });
        decreaseBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IncreaseOrDecrease = "Decreased";
                ShowDialog("Decrease  by ?");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String where = Contracts.InvetoryTable._ID + " = ?";
        String[] Args = {position + ""};
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contracts.InvetoryTable.QuantityAvailable, CurrentQuantity);
        int i = getContentResolver().update(uri, contentValues, where, Args);
    }

    private void ShowDialog(String hint) {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.custom, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setView(promptsView);
        final EditText textInputLayout = (EditText) promptsView.findViewById(R.id.txtNumber);
        textInputLayout.setHint(hint);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String str = textInputLayout.getText().toString();
                                if (str != null && !str.isEmpty())
                                    UpdateRaw(Integer.parseInt(textInputLayout.getText().toString()));
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void UpdateRaw(int num) {
        if (IncreaseOrDecrease.equals("Decreased")) {
            num *= -1;
        }
        int temp = CurrentQuantity + num;
        if (temp < 0) {
            Snackbar snackbar =
                    make(coordinatorLayout, "UnAvailable Quantity!", Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(getResources().getColor(R.color.error_color));
            snackbar.show();
            return;
        }
        CurrentQuantity = temp;
        Pquantity.setText("Quantity " + CurrentQuantity);
        //Toast.makeText(this, "Tmp " + temp + " Cu " + CurrentQuantity, Toast.LENGTH_SHORT).show();
        Snackbar snackbar =
                make(coordinatorLayout, IncreaseOrDecrease + " Successfully", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void ShowData() {
        c = getContentResolver().query(uri, null, null, null, null);
        c.moveToFirst();
        int q = c.getInt(c.getColumnIndex(Contracts.InvetoryTable.QuantityAvailable));
        Pname.setText("Product Name " + c.getString(c.getColumnIndex(Contracts.InvetoryTable.ProductName)));
        Pprice.setText("Product Price " + c.getString(c.getColumnIndex(Contracts.InvetoryTable.ProductPrice)));
        Pquantity.setText("Quantity " + q);
        Psupplier.setText("Supplier Email " + c.getString(c.getColumnIndex(Contracts.InvetoryTable.Supplier)));
        CurrentQuantity = q;
        //Toast.makeText(this, "Current " + CurrentQuantity, Toast.LENGTH_SHORT).show();
        byte[] blob = c.getBlob(c.getColumnIndex(Contracts.InvetoryTable.Image));
        Pimage.setImageBitmap(Utils.getImage(blob));
    }
}
