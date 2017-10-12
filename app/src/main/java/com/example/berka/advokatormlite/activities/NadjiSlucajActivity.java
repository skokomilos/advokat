package com.example.berka.advokatormlite.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Slucaj;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by berka on 22-Sep-17.
 */

public class NadjiSlucajActivity extends AppCompatActivity {

    EditText et_broj_slucaja;
    DatabaseHelper databaseHelper;
    Button button_nadji;

    public static String FIND_KEY = "FIND_KEY";

    String broj_slucaja;
    int i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nadji_slucaj);

        et_broj_slucaja = (EditText) findViewById(R.id.broj_slucaja);
        button_nadji = (Button) findViewById(R.id.btn_nadji_slucaj);


        button_nadji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    //todo ovde treba verovatno lista pa da se uzme prvi iz te liste
                    //stavljam u try/catch zbog mogucnosti da se ne unese broj
                    //pretvaram string u integer
                    try {
                        broj_slucaja = et_broj_slucaja.getText().toString();
                        i = Integer.parseInt(broj_slucaja);
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                    QueryBuilder<Slucaj, Integer> qb = getDatabaseHelper().getSlucajDao().queryBuilder();
                    qb.where().eq(Slucaj.BROJ_SLUCAJA, i);

                    List<Slucaj> sl = qb.query();

                    //sad treba uzeti taj objekat i otvoriti vrednosti proces/tarifa/radnja, samo prvi put treba da izabere proces posalji idPostupka iz tabele slucaja

                    //todo tabela bodova nekako povezati

                    Intent intent = new Intent(NadjiSlucajActivity.this, SecondActivity.class);
                    intent.putExtra(FIND_KEY, sl.get(0).getPostupak().getId());
                    startActivity(intent);

                    Log.d("Bilder", sl.get(0).getIme());

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });


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

        // nakon rada sa bazom podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
