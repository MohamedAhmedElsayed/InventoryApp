package com.example.mohamed_ahmed.inventoryapp.Activites;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mohamed_ahmed.inventoryapp.Data.Contracts;
import com.example.mohamed_ahmed.inventoryapp.Data.Cursor_Adapter;
import com.example.mohamed_ahmed.inventoryapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @BindView(R.id.AddProduct)
    FloatingActionButton FloatingAddProduct;
    @BindView(R.id.ProductList)
    ListView PList;
    CursorLoader cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getLoaderManager().initLoader(0, null, this);
        FloatingAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), AddProductActivity.class);
                startActivity(i);
            }
        });
        PList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getBaseContext(), DetailsActivity.class);
                TextView pid = (TextView) view.findViewById(R.id.PID);
                i.putExtra("RawPosition", pid.getText().toString());
                startActivity(i);
            }
        });
        PList.setEmptyView(findViewById(R.id.emptyTxt));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        cursor = new CursorLoader(this, Contracts.CONTENT_URI, null, null, null, null);
        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Cursor_Adapter Cursor_adapter = new Cursor_Adapter(this, data);
        PList.setAdapter(Cursor_adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
