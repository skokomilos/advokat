package com.example.berka.advokatormlite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.berka.advokatormlite.adapter.MyAdapter;
import com.example.berka.advokatormlite.adapter.MyAdapterTarifa;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Tarifa;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by berka on 7/21/2017.
 */

public class SecondActivity extends AppCompatActivity {

    ArrayList<Tarifa> tarifeLista;
    MyAdapterTarifa adapterTarifa;
    DatabaseHelper databaseHelper;
    Postupak postupak;
    int key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        key = getIntent().getExtras().getInt(MainActivity.POSTUPAK_KEY);

        try {
             postupak = getDatabaseHelper().getPostupakDao().queryForId(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView mListView = (ListView) findViewById(R.id.secondAListView);

        try {
            final ArrayList<Tarifa> tarife = (ArrayList<Tarifa>) getDatabaseHelper().getmTarifaDao().queryBuilder()
                    .where()
                    .eq(Tarifa.FIELD_TARIFA_POSTUPAK, postupak.getId())
                    .query();
            MyAdapterTarifa adapter = new MyAdapterTarifa(this, tarife);
            mListView.setAdapter(adapter);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public DatabaseHelper getDatabaseHelper(){
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazo podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
