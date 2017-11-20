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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.VrsteParnica;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by berka on 06-Nov-17.
 */

public class AddPrekrsajniActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper databaseHelper;

    private EditText sifra;
    private ScrollableNumberPicker brojStranaka;
    private Spinner tabelaBodovaSpinner;
    private Button addCase;

    private int postupak_id;
    private Postupak postupak;
    private TabelaBodova tabelaBodova;

    public static final String FROM = "from";
    public static final String CASE_ID = "case_id";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prekrsajni);

        initPostupak();
        initWidgets();
        loadSpinner();
        addCase.setOnClickListener(AddPrekrsajniActivity.this);

    }


    private void initWidgets(){

        sifra = (EditText) findViewById(R.id.sifraSlucajaPrekrsajni);
        brojStranaka = (ScrollableNumberPicker) findViewById(R.id.snp_horizontal_clients_prekrsajni);
        tabelaBodovaSpinner = (Spinner) findViewById(R.id.tabelaBodovaPrekrsajni);
        addCase = (Button) findViewById(R.id.button_add_prekrsajni);
    }

    private void loadSpinner() {

        final List<TabelaBodova> tabelaBodovaList;
        try {
            tabelaBodovaList = getDatabaseHelper().getmTabelaBodovaDao().queryBuilder()
                    .where()
                    .eq(TabelaBodova.POSTUPAK_ID, postupak.getId())
                    .query();
            ArrayAdapter<VrsteParnica> adapter = new ArrayAdapter(AddPrekrsajniActivity.this, android.R.layout.simple_spinner_item, tabelaBodovaList);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            tabelaBodovaSpinner.setAdapter(adapter);
            tabelaBodovaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    tabelaBodova = (TabelaBodova) tabelaBodovaSpinner.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void initPostupak(){

        postupak_id = getIntent().getExtras().getInt(ChoosePostupakActivity.POSTUPAK_KEY);
        Log.d("lista", String.valueOf(postupak_id));
        try {
            postupak = getDatabaseHelper().getPostupakDao().queryForId(postupak_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, postupak.getNazivpostupka(), Toast.LENGTH_SHORT).show();
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(AddPrekrsajniActivity.this, DatabaseHelper.class);
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

        //todo dodavanje u db sifrua, brojStranaka, okrivljen, tabelaBodova, postupak

        Slucaj slucaj = new Slucaj();
        slucaj.setBroj_slucaja(Integer.valueOf(sifra.getText().toString()));
        slucaj.setPostupak(postupak);
        slucaj.setTabelaBodova(tabelaBodova);
        Log.d("Vrednost", String.valueOf(brojStranaka.getValue()));
        slucaj.setBroj_stranaka(brojStranaka.getValue());


        try {
            getDatabaseHelper().getSlucajDao().create(slucaj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Toast.makeText(AddPrekrsajniActivity.this, "Novi je dodat\n"
                +slucaj.getBroj_slucaja() + "\n"
                +slucaj.getTabelaBodova() + "\n"
                +slucaj.getPostupak() + "\n"
                +slucaj.getBroj_stranaka(), Toast.LENGTH_LONG).show();

        Intent i = new Intent(AddPrekrsajniActivity.this, PronadjeniSlucaj.class);
        i.putExtra(FROM, "add");
        i.putExtra(CASE_ID, slucaj.getId());
        startActivity(i);
    }
}
