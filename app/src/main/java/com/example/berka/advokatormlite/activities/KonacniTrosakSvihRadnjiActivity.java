package com.example.berka.advokatormlite.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.ListView;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.adapter.SviTroskviAdapter;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.IzracunatTrosakRadnje;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by berka on 11-Oct-17.
 */

public class KonacniTrosakSvihRadnjiActivity extends AppCompatActivity{

    DatabaseHelper databaseHelper;
    Postupak postupak;
    Slucaj slucaj;
    int key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konacni_trosak_svih_radnji);

        key = getIntent().getExtras().getInt(SecondActivity.SLUCAJ_KEY);

        try {
            slucaj = getDatabaseHelper().getSlucajDao().queryForId(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = (ListView) findViewById(R.id.lista_svih_uradjenih_radnji);

            try {
                final List<IzracunatTrosakRadnje> sveIzracunateRadnje = getDatabaseHelper().getmIzracunatTrosakRadnjeDao().queryBuilder()
                        .where()
                        .eq(IzracunatTrosakRadnje.SLUCAJ_ID, slucaj.getId())
                        .query();
                SviTroskviAdapter adapter = new SviTroskviAdapter(this, (ArrayList<IzracunatTrosakRadnje>) sveIzracunateRadnje);
                listView.setAdapter(adapter);


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
