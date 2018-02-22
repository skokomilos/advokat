package com.example.berka.advokatormlite.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.adapter.StrankaDynamicViews;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.interfaces.IDynamicallyAddEditTexts;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.VrsteParnica;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by berka on 06-Nov-17.
 */

public class AddPrekrsajniActivity extends AppCompatActivity implements IDynamicallyAddEditTexts{

    private DatabaseHelper databaseHelper;

    private Context context;

    private EditText et_sifra_slucaja;
    private int broj_stranaka;
    private Spinner tabelaBodovaSpinner;

    private StrankaDynamicViews strankaDynamicViews;
    private StrankaDetail strankaDetail;
    private List<StrankaDetail> strankaDetailsList;
    private GridLayout gridLayout;
    private int redni_broj_prvog_et_u_grid_ll = 2;
    private static final String TAG = "Ljog";

    @BindView(R.id.button_add_prekrsajni)
    Button btnAddCase;

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
        addEditTextsProgramabilno();
        ButterKnife.bind(AddPrekrsajniActivity.this);
        initWidgets();
        loadSpinner();

    }

    @Override
    public void initPostupak(){
        postupak_id = getIntent().getExtras().getInt(MainActivity.POSTUPAK_KEY);
        broj_stranaka = getIntent().getExtras().getInt(MainActivity.BROJ_STRANAKA);
        Log.d("Ljog", String.valueOf(broj_stranaka));
        gridLayout = findViewById(R.id.prekrsaj_gridlayout_za_dinamicko_dodavanje_polja);
        Log.d("lista", String.valueOf(postupak_id));
        try {
            postupak = getDatabaseHelper().getPostupakDao().queryForId(postupak_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, postupak.getNazivpostupka(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addEditTextsProgramabilno() {
        for (int i = 0; i < broj_stranaka; i++) {
            strankaDynamicViews = new StrankaDynamicViews(context);
            gridLayout.addView(strankaDynamicViews.strankaNumTextView(getApplicationContext(), String.valueOf(i + 1)));
            gridLayout.addView(strankaDynamicViews.recivedImeEditText(getApplicationContext()));
            gridLayout.addView(strankaDynamicViews.recivedAdersaEditText(getApplicationContext()));
            gridLayout.addView(strankaDynamicViews.recivedMestoEditText(getApplicationContext()));
        }
    }

    @Override
    public ArrayList<StrankaDetail> allStrankeFromListViewToArrayList() {
        ArrayList<StrankaDetail> myArrayListSvihStranakaSaPodacima = new ArrayList<>();
        for(int i=0; i<broj_stranaka; i++) {

            //getValue(input);
            myArrayListSvihStranakaSaPodacima.add(getVrednostiSvihPoljaZaStranku(redni_broj_prvog_et_u_grid_ll));
            redni_broj_prvog_et_u_grid_ll+=4;
            Log.d("Velicina", String.valueOf(myArrayListSvihStranakaSaPodacima.size()));
            Log.d("Imeprezime", strankaDetail.getIme_i_prezime());
        }
        return myArrayListSvihStranakaSaPodacima;
    }

    @Override
    public StrankaDetail getVrednostiSvihPoljaZaStranku(int position) {
        strankaDetail = new StrankaDetail();
        EditText imeprezime = (EditText) gridLayout.getChildAt(position);
        EditText adresa = (EditText) gridLayout.getChildAt(position+1);
        EditText mesto = (EditText) gridLayout.getChildAt(position+2);

        strankaDetail.setIme_i_prezime(imeprezime.getText().toString());
        strankaDetail.setAdresa(adresa.getText().toString());
        strankaDetail.setMesto(mesto.getText().toString());

        return strankaDetail;
    }

    private void initWidgets(){

        et_sifra_slucaja = (EditText) findViewById(R.id.sifraSlucajaPrekrsajni);
        tabelaBodovaSpinner = (Spinner) findViewById(R.id.tabelaBodovaPrekrsajni);
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
                    Log.d(TAG, "onItemSelected: " + tabelaBodova);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.button_add_prekrsajni)
    public void addSlucaj() {

        Slucaj slucaj = new Slucaj();

        try {
            if (et_sifra_slucaja.getText().toString() == null || et_sifra_slucaja.getText().toString().trim().equals("")) {
                Toast.makeText(AddPrekrsajniActivity.this, "Morate uneti sifru slucaja", Toast.LENGTH_LONG).show();
            } else {
                slucaj.setBroj_slucaja(Integer.valueOf(et_sifra_slucaja.getText().toString()));
                Log.d(TAG, "addSlucaj: " + et_sifra_slucaja.getText().toString());
                slucaj.setPostupak(postupak);
                slucaj.setTabelaBodova(tabelaBodova);
                slucaj.setBroj_stranaka(broj_stranaka);

                Log.d(TAG, "addSlucaj: " + +slucaj.getBroj_slucaja() + "\n"
                        +slucaj.getTabelaBodova() + "\n"
                        +slucaj.getPostupak() + "\n"
                        +slucaj.getBroj_stranaka());
            }
        }catch (NumberFormatException e){
            Toast.makeText(this, "Za testiranje dozvoljen unos samo brojeva", Toast.LENGTH_LONG).show();
        }

        try {
            getDatabaseHelper().getSlucajDao().create(slucaj);

            Intent intent = new Intent(AddPrekrsajniActivity.this, PronadjeniSlucaj.class);
            intent.putExtra(FROM, "add");
            intent.putExtra(CASE_ID, slucaj.getId());
            startActivity(intent);

            Toast.makeText(AddPrekrsajniActivity.this, "Novi je dodat\n"
                    +slucaj.getBroj_slucaja() + "\n"
                    +slucaj.getTabelaBodova() + "\n"
                    +slucaj.getPostupak() + "\n"
                    +slucaj.getBroj_stranaka(), Toast.LENGTH_LONG).show();

            strankaDetailsList = allStrankeFromListViewToArrayList();
            for (int i = 0; i < strankaDetailsList.size(); i++) {
                strankaDetailsList.get(i).setSlucaj(slucaj);
                getDatabaseHelper().getmStrankaDetail().create(strankaDetailsList.get(i));
                Log.d(TAG, "addSlucaj: dodat jos jedan u listu");
            Log.d(TAG, "addSlucaj: 2");
                     }
        }catch (SQLiteConstraintException e){
            e.printStackTrace();
        }
        catch (SQLException e) {
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
}
