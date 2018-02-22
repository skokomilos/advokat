package com.example.berka.advokatormlite.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.adapter.StrankaDynamicViews;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.interfaces.IDynamicallyAddEditTexts;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by berka on 06-Nov-17.
 */

public class AddUstavniActivity extends AppCompatActivity implements IDynamicallyAddEditTexts {

    private static final String FROM = "from";
    private static final String CASE_ID = "case_id";
    private DatabaseHelper databaseHelper;

    @BindView(R.id.sifraSlucajaUstavni)
    EditText et_sifra;

    @BindView(R.id.button_add_ustavni)
    Button addSlucaj;
    
    @BindView(R.id.ustavni_gridlayout_za_dinamicko_dodavanje_polja)
    GridLayout gridLayout;
    private Postupak postupak;
    private int broj_stranaka;
    private StrankaDynamicViews strankaDynamicViews;
    private Context context;
    private StrankaDetail strankaDetail;
    private int redni_broj_prvog_et_u_grid_ll;
    private ArrayList<StrankaDetail> strankaDetailsList;


    public static final int TABELA_BODOVA_ID = 47;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ustavni);

        ButterKnife.bind(AddUstavniActivity.this);
        
    }

    @OnClick(R.id.button_add_ustavni)
    public void addSlucaj(){

        StrankaDetail strankaDetail = new StrankaDetail();

        Slucaj slucaj = new Slucaj();

        try {
            if (et_sifra.getText().toString() == null || et_sifra.getText().toString().trim().equals("")) {
                Toast.makeText(AddUstavniActivity.this, "Morate uneti sifru slucaja", Toast.LENGTH_LONG).show();
            } else {
                slucaj.setBroj_slucaja(Integer.valueOf(et_sifra.getText().toString()));
                slucaj.setPostupak(postupak);
                //slucaj.setTabelaBodova(tabelaBodovaObj);
                slucaj.setBroj_stranaka(broj_stranaka);
            }
        }catch (NumberFormatException e){
            Toast.makeText(this, "Za testiranje dozvoljen unos samo brojeva", Toast.LENGTH_LONG).show();
        }

        try {
            getDatabaseHelper().getSlucajDao().create(slucaj);

            Intent intent = new Intent(AddUstavniActivity.this, PronadjeniSlucaj.class);
            intent.putExtra(FROM, "add");
            intent.putExtra(CASE_ID, slucaj.getId());
            startActivity(intent);

            Toast.makeText(AddUstavniActivity.this, "Novi je dodat\n"
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
    
    public void initPostupak(){
        int postupak_id = getIntent().getExtras().getInt(MainActivity.POSTUPAK_KEY);
        broj_stranaka = getIntent().getExtras().getInt(MainActivity.BROJ_STRANAKA);
        try {
            postupak = getDatabaseHelper().getPostupakDao().queryForId(postupak_id);
        } catch (SQLException e) {
            e.printStackTrace();
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
