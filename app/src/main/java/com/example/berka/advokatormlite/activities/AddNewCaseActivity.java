package com.example.berka.advokatormlite.activities;

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
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by berka on 04-Oct-17.
 */

public class AddNewCaseActivity  extends AppCompatActivity{
     //implements AdapterView.OnItemSelectedListener

    private DatabaseHelper databaseHelper;
    EditText et_ime, et_prezime, et_broj_slucaja;
    Spinner spinerPostupak, spinerTabelaBodova;
    Button bt_add_case;

    Postupak postupakObject;
    private TabelaBodova tabelaBodovaObject;


    private int postupak_id;
    private int tabela_id;


//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_new_case);
//
//        initWidgets();
//
//        spinerPostupak.setOnItemSelectedListener(this);
//        spinerTabelaBodova.setOnItemSelectedListener(this);
//
//        openSpinners();
//
//        bt_add_case.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Slucaj slucaj = new Slucaj();
////                slucaj.setIme(et_ime.getText().toString());
////                slucaj.setPrezime(et_prezime.getText().toString());
//                slucaj.setBroj_slucaja(Integer.valueOf(et_broj_slucaja.getText().toString()));
//                try {
//                    postupakObject = getDatabaseHelper().getPostupakDao().queryForId(postupak_id);
//                    tabelaBodovaObject = getDatabaseHelper().getmTabelaBodovaDao().queryForId(tabela_id);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//                slucaj.setPostupak(postupakObject);
//                slucaj.setTabelaBodova(tabelaBodovaObject);
//
//
//                try {
//                    getDatabaseHelper().getSlucajDao().create(slucaj);
//                    Toast.makeText(AddNewCaseActivity.this, "Novi je dodat\n"
//                            +slucaj.getBroj_slucaja() + "\n"
//                            +slucaj.getPostupak() + "\n"
//                            +slucaj.getTabelaBodova() + "\n", Toast.LENGTH_LONG).show();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//       // bt_add_case.setOnClickListener(this);
//    }
//
//
//    // metoda za expandable spiner, po poetnim vrednostima ce biti krivica i kazna zatvora od 3 do 5 godina
//    private void openSpinners(){
//
//        try {
//            final List<Postupak> postupciList = getDatabaseHelper().getPostupakDao().queryForAll();
//            ArrayAdapter<Postupak> adapter = new ArrayAdapter(AddNewCaseActivity.this, android.R.layout.simple_spinner_item, postupciList);
//            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//            //Log.d("Postupak", postupciList.get(0).getNazivpostupka());
//            spinerPostupak.setAdapter(adapter);
//
//            spinerPostupak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//
//                    //pritisnuti item iz spinera pretvaram u objekat klase postupak
//                    //ovde postoji problem, kad odaberemo PARNICA postoje dve vrste parnica (procenjena i nerpocenjena) kad se odabere PARNICA prikazuje se novi spiner koji nudi procenjeno i nerpocenjeno
//                    //i ispod tabelu bodova tj (zaprecenu kaznu)
//                    postupakObject = (Postupak) spinerPostupak.getSelectedItem();
//                    postupak_id = postupakObject.getId();
//                    Toast.makeText(AddNewCaseActivity.this, String.valueOf(postupak_id), Toast.LENGTH_SHORT).show();
//                    try {
//                        //pozivam listu klase tabelaBodova uz pomoc id-a objekta klase postupak koji sam gore dobio
//                       final List<TabelaBodova> list = getDatabaseHelper().getmTabelaBodovaDao().queryBuilder()
//                                .where()
//                                .eq(TabelaBodova.POSTUPAK_ID, postupakObject.getId())
//                                .query();
//                            ArrayAdapter<TabelaBodova> adapter1 = new ArrayAdapter(AddNewCaseActivity.this, android.R.layout.simple_spinner_item, list);
//                            adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//                            spinerTabelaBodova.setAdapter(adapter1);
//                            tabelaBodovaObject = (TabelaBodova) spinerTabelaBodova.getSelectedItem();
//                            tabela_id = tabelaBodovaObject.getId();
//                            Toast.makeText(AddNewCaseActivity.this, String.valueOf(tabela_id), Toast.LENGTH_SHORT).show();
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    private void initWidgets(){
//
//        et_ime = (EditText) findViewById(R.id.et_add_name);
//        et_prezime = (EditText) findViewById(R.id.et_add_lastname);
//        et_broj_slucaja = (EditText) findViewById(R.id.et_add_number_case);
//        spinerPostupak = (Spinner) findViewById(R.id.spinnerPostupak);
//        spinerTabelaBodova = (Spinner) findViewById(R.id.spinnerTabelaBodova);
//        bt_add_case = (Button) findViewById(R.id.buttonAddNewCase);
//
//    }
//
//    public DatabaseHelper getDatabaseHelper() {
//        if (databaseHelper == null) {
//            databaseHelper = OpenHelperManager.getHelper(AddNewCaseActivity.this, DatabaseHelper.class);
//        }
//        return databaseHelper;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        if (databaseHelper != null) {
//            OpenHelperManager.releaseHelper();
//            databaseHelper = null;
//        }
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//
//        switch (adapterView.getId())
//        {
//            case R.id.spinnerPostupak:
//                Slucaj slucaj = (Slucaj) adapterView.getItemAtPosition(position);
//                Toast.makeText(this, String.valueOf(slucaj.getId()), Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.spinnerTabelaBodova:
//                TabelaBodova tabelaBodova = (TabelaBodova) adapterView.getItemAtPosition(position);
//                Toast.makeText(this, String.valueOf(tabelaBodova.getId()), Toast.LENGTH_SHORT).show();
//                break;
//            default:
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }
}
