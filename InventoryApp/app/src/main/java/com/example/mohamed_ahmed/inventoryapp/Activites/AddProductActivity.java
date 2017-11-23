package com.example.mohamed_ahmed.inventoryapp.Activites;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mohamed_ahmed.inventoryapp.Data.Contracts;
import com.example.mohamed_ahmed.inventoryapp.Data.Utils;
import com.example.mohamed_ahmed.inventoryapp.R;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddProductActivity extends AppCompatActivity {
    private static final int LOAD_IMAGE_RESULTS = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    @BindView(R.id.Name)
    EditText PnameView;
    @BindView(R.id.Price)
    EditText PpriceView;
    @BindView(R.id.Supplier)
    EditText PsupplierView;
    @BindView(R.id.Quantity)
    EditText PQuantityView;
    @BindView(R.id.AddImage)
    ImageView Pimage;
    @BindView(R.id.ClickToAdd)
    Button ClickToAdd;
    @BindView(R.id.btnSelectImage)
    FloatingActionButton flotBut;
    Uri SelectedImageUri;
    boolean ValidData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);
        ClickToAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageSelector();
            }
        });
        flotBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AddProduct();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void AddProduct() throws IOException {
        String Pname = PnameView.getText().toString(),
                Pprice = PpriceView.getText().toString(),
                PQuantity = PQuantityView.getText().toString(),
                Psupplier = PsupplierView.getText().toString();
        ValidData = true;
        if (Pname.isEmpty() || Pname == null) {
            ValidData = false;
            PnameView.setError("Enter Product Name");
        }
        if (Pprice.isEmpty() || Pprice == null) {
            ValidData = false;
            PpriceView.setError("Enter Product Price");
        }
        if (Psupplier.isEmpty() || Psupplier == null) {
            ValidData = false;
            PsupplierView.setError("Invalid Email");
        }
        if (PQuantity.isEmpty() || PQuantity == null) {
            ValidData = false;
            PQuantityView.setError("Enter Product quantity");
        }
        if (SelectedImageUri == null) {
            if (ValidData)
                Toast.makeText(this, "Select Product Image", Toast.LENGTH_LONG).show();
            ValidData = false;
        }
        if (ValidData) {
            InputStream iStream = getContentResolver().openInputStream(SelectedImageUri);
            byte[] buffer = Utils.getBytes(iStream);
            ContentValues values = new ContentValues();
            values.put(Contracts.InvetoryTable.ProductName, Pname);
            values.put(Contracts.InvetoryTable.ProductPrice, Pprice);
            values.put(Contracts.InvetoryTable.Supplier, Psupplier);
            values.put(Contracts.InvetoryTable.QuantityAvailable, PQuantity);
            values.put(Contracts.InvetoryTable.Image, buffer);
            Uri uri = getContentResolver().insert(Contracts.CONTENT_URI, values);
            Toast.makeText(this, "Saved ", Toast.LENGTH_LONG).show();
            // Cursor c = getContentResolver().query(Contracts.CONTENT_URI, null, null, null, null);
        }
    }

    private void openImageSelector() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, LOAD_IMAGE_RESULTS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            SelectedImageUri = data.getData();
            if (SelectedImageUri != null) {
                ValidData = true;
                Pimage.setImageURI(SelectedImageUri);
            } else {
                ValidData = false;
            }
        }
    }
}

 