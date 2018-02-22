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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by berka on 19-Oct-17.
 */

public class AddKrivicaActivity extends AppCompatActivity implements IDynamicallyAddEditTexts{

    @BindView(R.id.et_sifra_add_krivica)
    EditText et_sifra_slucaja;

    @BindView(R.id.okrivljen_ostecen_radio_group)
    RadioGroup radioGroup;

    @BindView(R.id.spinnerKrivicaKazne)
    Spinner spinner;

    @BindView(R.id.btn_dodaj_krivicu)
    Button button;

    private GridLayout gridLayout;
    private StrankaDynamicViews strankaDynamicViews;
    private StrankaDetail strankaDetail;
    private List<StrankaDetail> strankaDetailsList;
    private Context context;

    private DatabaseHelper databaseHelper;
    private Postupak postupak;
    private TabelaBodova tabelaBodovaObj;
    private int postupak_id, broj_stranaka;

    int redni_broj_prvog_et_u_grid_ll = 2;

    public static final String FROM = "from";
    public static final String CASE_ID = "case_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_krivica);

        initPostupak();
        addEditTextsProgramabilno();
        ButterKnife.bind(AddKrivicaActivity.this);
        loadSpinner();
    }

    @Override
    public void initPostupak() {
        gridLayout = findViewById(R.id.krivica_gridlayout_za_dinamicko_dodavanje_polja);
        postupak_id = getIntent().getExtras().getInt(MainActivity.POSTUPAK_KEY);
        broj_stranaka = getIntent().getExtras().getInt(MainActivity.BROJ_STRANAKA);
        try {
            postupak = getDatabaseHelper().getPostupakDao().queryForId(postupak_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void loadSpinner(){
        try {
            final List<TabelaBodova> zapreceneKazneList = (List<TabelaBodova>) getDatabaseHelper().getmTabelaBodovaDao().queryBuilder()
                    .where()
                    .eq(TabelaBodova.POSTUPAK_ID, postupak_id)
                    .query();

            ArrayAdapter adapter = new ArrayAdapter(AddKrivicaActivity.this, android.R.layout.simple_spinner_dropdown_item, zapreceneKazneList);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    tabelaBodovaObj = (TabelaBodova) spinner.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_dodaj_krivicu)
    public void addSlucaj(){

        int broj = 0;

        String radiovalue=  ((RadioButton)this.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
        if(radiovalue.equals("Okrivljen")){
            broj = 1;
        }else if(radiovalue.equals("Ostecen")){
            broj = 0;
        }

        StrankaDetail strankaDetail = new StrankaDetail();

        Slucaj slucaj = new Slucaj();

        try {
            if (et_sifra_slucaja.getText().toString() == null || et_sifra_slucaja.getText().toString().trim().equals("")) {
                Toast.makeText(AddKrivicaActivity.this, "Morate uneti sifru slucaja", Toast.LENGTH_LONG).show();
            } else {
                slucaj.setBroj_slucaja(Integer.valueOf(et_sifra_slucaja.getText().toString()));
                slucaj.setPostupak(postupak);
                slucaj.setTabelaBodova(tabelaBodovaObj);
                slucaj.setOkrivljen(broj);
                slucaj.setBroj_stranaka(broj_stranaka);
            }
        }catch (NumberFormatException e){
            Toast.makeText(this, "Za testiranje dozvoljen unos samo brojeva", Toast.LENGTH_LONG).show();
        }

        try {
            getDatabaseHelper().getSlucajDao().create(slucaj);

            Intent intent = new Intent(AddKrivicaActivity.this, PronadjeniSlucaj.class);
            intent.putExtra(FROM, "add");
            intent.putExtra(CASE_ID, slucaj.getId());
            startActivity(intent);

            Toast.makeText(AddKrivicaActivity.this, "Novi je dodat\n"
                    +slucaj.getBroj_slucaja() + "\n"
                    +slucaj.getTabelaBodova() + "\n"
                    +slucaj.getPostupak() + "\n"
                    +slucaj.getBroj_stranaka(), Toast.LENGTH_LONG).show();


            strankaDetailsList = allStrankeFromListViewToArrayList();
            for (int i = 0; i < strankaDetailsList.size(); i++) {
                strankaDetailsList.get(i).setSlucaj(slucaj);
                getDatabaseHelper().getmStrankaDetail().create(strankaDetailsList.get(i));
                Log.d("ListaDodat", "lista dodata");
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
