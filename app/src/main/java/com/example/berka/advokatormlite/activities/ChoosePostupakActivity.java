package com.example.berka.advokatormlite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by berka on 19-Oct-17.
 */

public class ChoosePostupakActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseHelper databaseHelper;
    private Postupak postupakObject;
    private Spinner spinerPostupak;
    private Button go_to_expandable_list_meny_button;

    public static String POSTUPAK_KEY = "POSTUPAK_KEY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_postupak);

        spinerPostupak = (Spinner) findViewById(R.id.spinnerPostupak);
        go_to_expandable_list_meny_button = (Button) findViewById(R.id.btnChoosePostupakActivity);
        go_to_expandable_list_meny_button.setOnClickListener(ChoosePostupakActivity.this);
        loadSpinner();
    }

    private void loadSpinner(){
        try {
            final List<Postupak> postupciList = getDatabaseHelper().getPostupakDao().queryForAll();
            ArrayAdapter<Postupak> adapter = new ArrayAdapter(ChoosePostupakActivity.this, android.R.layout.simple_spinner_item, postupciList);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinerPostupak.setAdapter(adapter);
            spinerPostupak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    postupakObject = (Postupak) spinerPostupak.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(this);
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


    @Override
    public void onClick(View v) {

        //todo verovatno ovde dodati i.putextra(case_type);
        //todo odavde idem direkt na PronadjeniSlucaj klasu (ovo ime klse moram da promenim u nesto razumnije)

        Intent i;
        switch (postupakObject.getId()) {
            case 2:
                //add krivica
                i = new Intent(ChoosePostupakActivity.this, AddKrivicaActivity.class);
                i.putExtra(POSTUPAK_KEY, postupakObject.getId());
                startActivity(i);
                break;
            case 4:
                //add prekrsajni postupak
                        i = new Intent(ChoosePostupakActivity.this, AddPrekrsajniActivity.class);
                        i.putExtra(POSTUPAK_KEY, postupakObject.getId());
                        startActivity(i);
                break;
            case 13:
                //add isprave
                        i = new Intent(ChoosePostupakActivity.this, AddUstavniActivity.class);
                        i.putExtra(POSTUPAK_KEY, postupakObject.getId());
                        startActivity(i);
                break;
            default:
                //add sve ostale koji imaju procenjene i neprocenjene predmete
                i = new Intent(ChoosePostupakActivity.this, AddParnicaActivity.class);
                i.putExtra(POSTUPAK_KEY, postupakObject.getId());
                startActivity(i);
                break;
        }
    }
}
