package com.example.berka.advokatormlite.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.adapter.SviTroskviAdapter;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.IzracunatTrosakRadnje;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by berka on 11-Oct-17.
 */

public class KonacniTrosakSvihRadnjiActivity extends AppCompatActivity{

    DatabaseHelper databaseHelper;
    private Postupak postupak;
    private Slucaj slucaj;
    private List<IzracunatTrosakRadnje> sveIzracunateRadnje;

    private TextView tv_ukupna_cena;

    private int key;
    private int ukupnaCenaSvihRadnji = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konacni_trosak_svih_radnji);

        loadSlucaj();
        loadSpinner();
        ukupnaCena();
    }

    private void loadSlucaj(){
        key = getIntent().getExtras().getInt(PronadjeniSlucaj.SLUCAJ_KEY);
        try {
            slucaj = getDatabaseHelper().getSlucajDao().queryForId(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSpinner(){
        final ListView listView = (ListView) findViewById(R.id.lista_svih_uradjenih_radnji);
        try {
            sveIzracunateRadnje = getDatabaseHelper().getmIzracunatTrosakRadnjeDao().queryBuilder()
                    .where()
                    .eq(IzracunatTrosakRadnje.SLUCAJ_ID, slucaj.getId())
                    .query();
            SviTroskviAdapter adapter = new SviTroskviAdapter(this, (ArrayList<IzracunatTrosakRadnje>) sveIzracunateRadnje);
            listView.setAdapter(adapter);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ukupnaCena(){

        tv_ukupna_cena = (TextView) findViewById(R.id.tv_ukupna_cena_svih_radnji);

        ListIterator<IzracunatTrosakRadnje> iterator = null;
        iterator = sveIzracunateRadnje.listIterator();
        IzracunatTrosakRadnje iztr;
        while (iterator.hasNext()){
            iztr = iterator.next();
            ukupnaCenaSvihRadnji+=iztr.getCena_izracunate_jedinstvene_radnje();
        }
        tv_ukupna_cena.setText(String.valueOf(ukupnaCenaSvihRadnji));

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
