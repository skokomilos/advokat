package com.example.berka.advokatormlite.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

/**
 * Created by berka on 06-Nov-17.
 */

public class AddUstavniActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private EditText sifra;
    private ScrollableNumberPicker numOfClients;
    private Button addSlucaj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ustavni);



    }

    private void initWidgets(){

        sifra = (EditText) findViewById(R.id.sifraSlucajaUstavni);
        numOfClients = (ScrollableNumberPicker) findViewById(R.id.snp_horizontal_clients_ustavni);
        addSlucaj = (Button) findViewById(R.id.button_add_ustavni);

    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(AddUstavniActivity.this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }


}
