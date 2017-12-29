package com.example.berka.advokatormlite.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.adapter.MyAdapterSviSlucajevi;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.IzracunatTrosakRadnje;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    //sources:
    //https://github.com/andreasschrade/android-design-template
    //https://github.com/afollestad/material-dialogs

    private DatabaseHelper databaseHelper;
    public static final String FROM = "from";
    public static final String CASE_ID = "case_id";
    public static String POSTUPAK_KEY = "POSTUPAK_KEY";
    public static String BROJ_STRANAKA = "BROJ_STRANAKA";
    private Postupak postupak;
//    private CardView cv_add, cv_find, cv_all, cv_myprofile;


    public static final String FROM_MAIN = "from_main";
    public static final String CASE_TYPE = "case_type";

    Toast toast;


    @BindView(R.id.cv_find)
    CardView cv_find;

    @BindView(R.id.cv_add)
    CardView cv_add;

    @BindView(R.id.cv_all)
    CardView cv_all;

    private List<Slucaj> slucajevi;

    String str_broj_slucaja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_test);


        setupToolbar();
        ButterKnife.bind(MainActivity.this);

    }

    //tri metoda pomocu kojih otvaram dijaloge za add, find, edit
    @OnClick(R.id.cv_find)
    public void openDialogFind1(){

        new MaterialDialog.Builder(this)
                .title("Moj Dialog")
                .content("Unesi sifru slucaja")
                .inputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                        | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(1, 16)
                .positiveText("Submit")
                .input("", "", false,
                        (dialog, input) -> findCase(input.toString())).show();
    }


    private void findCase(String string){
        Log.d("Unesena sifra", string);
        if(string == null || string.trim().equals("")){
            Toast.makeText(MainActivity.this, "Morate uneti broj", Toast.LENGTH_LONG).show();
        }else {
            QueryBuilder<Slucaj, Integer> qb = null;
            try {
                qb = getDatabaseHelper().getSlucajDao().queryBuilder();
                qb.where().eq(Slucaj.BROJ_SLUCAJA, Integer.parseInt(string));

                List<Slucaj> slucajevi = qb.query();

                Intent intent = new Intent(MainActivity.this, PronadjeniSlucaj.class);
                intent.putExtra(FROM, "find");
                intent.putExtra(CASE_ID, slucajevi.get(0).getId());
                startActivity(intent);
            }catch (IndexOutOfBoundsException e){
                Toast.makeText(this, "Slucaj sa tom sifrom ne postoji", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.cv_add)
    public void addNewCase1(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_case, null);
        mBuilder.setTitle("Izaberi postupak");

        Spinner spinner = mView.findViewById(R.id.spnDialogAdd);
        EditText broj_stranaka = mView.findViewById(R.id.broj_stranaka);
        broj_stranaka.setText("1");

        final List<Postupak> postupciList;
        try {
            postupciList = getDatabaseHelper().getPostupakDao().queryForAll();
            ArrayAdapter<Postupak> adapter = new ArrayAdapter<Postupak>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, postupciList);
            spinner.setAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Postupak postupakObject = (Postupak) spinner.getSelectedItem();
                Intent intent;
                switch (postupakObject.getId()) {
                    case 2:
                        //add krivica
                        intent = new Intent(MainActivity.this, AddKrivicaActivity.class);
                        intent.putExtra(POSTUPAK_KEY, postupakObject.getId());
                        intent.putExtra(BROJ_STRANAKA, Integer.parseInt(broj_stranaka.getText().toString()));
                        startActivity(intent);
                        break;
                    case 4:
                        //add prekrsajni postupak
                        intent = new Intent(MainActivity.this, AddPrekrsajniActivity.class);
                        intent.putExtra(POSTUPAK_KEY, postupakObject.getId());
                        intent.putExtra(BROJ_STRANAKA, Integer.parseInt(broj_stranaka.getText().toString()));
                        startActivity(intent);
                        break;
                    case 13:
                        //add isprave
                        intent = new Intent(MainActivity.this, AddUstavniActivity.class);
                        intent.putExtra(POSTUPAK_KEY, postupakObject.getId());
                        intent.putExtra(BROJ_STRANAKA, Integer.parseInt(broj_stranaka.getText().toString()));
                        startActivity(intent);
                        break;
                    default:
                        //add sve ostale koji imaju procenjene i neprocenjene predmete
                        intent = new Intent(MainActivity.this, AddParnicaActivity.class);
                        intent.putExtra(POSTUPAK_KEY, postupakObject.getId());
                        intent.putExtra(BROJ_STRANAKA, Integer.parseInt(broj_stranaka.getText().toString()));
                        startActivity(intent);
                        break;
                }
            }
        });
        mBuilder.setNegativeButton("Dissmiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


    @OnClick(R.id.cv_all)
    public void showaLL(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_all_cases, null);
        mBuilder.setTitle("Izaberi postupak");
        ListView listView = mView.findViewById(R.id.lista_svih_slucajeva);

        ArrayList<Double> listaTrenutneCene = new ArrayList<>();

        try {
            slucajevi = getDatabaseHelper().getSlucajDao().queryForAll();
            listaTrenutneCene = (ArrayList<Double>) ukupnaCena(slucajevi);
            MyAdapterSviSlucajevi myadaper = new MyAdapterSviSlucajevi(MainActivity.this, (ArrayList<Slucaj>) slucajevi, listaTrenutneCene);
            listView.setAdapter(myadaper);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        mBuilder.setNegativeButton("Izadji", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private List<Double> ukupnaCena(List<Slucaj> slc){
        double trenutnaVrednost = 0;
        ArrayList<Double> listaSvhihTrenutnihCena = new ArrayList<>();
        ListIterator<Slucaj> iterator = null;
        iterator = slc.listIterator();
        Slucaj s;
        while (iterator.hasNext()){
            s = iterator.next();
            for (IzracunatTrosakRadnje i : s.getListaIzracunatihTroskovaRadnji()){
                trenutnaVrednost+=i.getCena_izracunate_jedinstvene_radnje();
            }
            listaSvhihTrenutnihCena.add(Double.valueOf(trenutnaVrednost));
            trenutnaVrednost = 0;
        }
        return listaSvhihTrenutnihCena;
    }

    //toolbar metode
    //5 metoda za nav/toolbar
    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.app_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_quotes;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }


    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
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