package com.example.berka.advokatormlite.activities;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.PostupakVrstaParniceJoin;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.VrsteParnica;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by berka on 19-Oct-17.
 */

public class AddParnicaActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseHelper databaseHelper;
    int postupak_id, broj_stranaka;

    public static final String FROM = "from";
    public static final String CASE_ID = "case_id";

    TabelaBodova tabelaBodovaObj;
    Postupak postupak;
    VrsteParnica vrsteParnicaObjekat;


    EditText sifraSlucaja;
    Spinner spinnerVrsteParnica, spinnerTabelaBodova;
    Button btnAddSlucajParnica;
    ScrollableNumberPicker numOfClients;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parnica);

        initPostupak();
        initWidgets();
        try {
            loadSpinnersTest();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        btnAddSlucajParnica.setOnClickListener(AddParnicaActivity.this);

    }


    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(AddParnicaActivity.this, DatabaseHelper.class);
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

    private void initPostupak(){

        postupak_id = getIntent().getExtras().getInt(MainActivity.POSTUPAK_KEY);
        broj_stranaka = getIntent().getExtras().getInt(MainActivity.BROJ_STRANAKA);
        Log.d("testiranje", String.valueOf(postupak_id));
        Log.d("broj stranaka", String.valueOf(broj_stranaka));
        try {
            postupak = getDatabaseHelper().getPostupakDao().queryForId(postupak_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, postupak.getNazivpostupka(), Toast.LENGTH_SHORT).show();
    }

    private void initWidgets(){

        spinnerVrsteParnica = (Spinner) findViewById(R.id.spinnerVrsteParnice);
        spinnerTabelaBodova = (Spinner) findViewById(R.id.spinnerKazne);
        btnAddSlucajParnica = (Button) findViewById(R.id.btnAddSlucajParnica);
        sifraSlucaja = (EditText) findViewById(R.id.sifraSlucajaParnica);
        numOfClients = (ScrollableNumberPicker) findViewById(R.id.snp_horizontal_clients);
    }

    ///////////////metode many to many  https://github.com/j256/ormlite-jdbc/tree/master/src/test/java/com/j256/ormlite/examples/manytomany

    private PreparedQuery<Postupak> postupciForVrsteParnicaQuery = null;

    private List<Postupak> lookUpPostupciForVrstaParnica(VrsteParnica vrsteParnica) throws SQLException{
        if(postupciForVrsteParnicaQuery == null){
            postupciForVrsteParnicaQuery = makePostupciForVrsteParnica();
        }
        postupciForVrsteParnicaQuery.setArgumentHolderValue(0, vrsteParnica);
        return databaseHelper.getPostupakDao().query(postupciForVrsteParnicaQuery);
    }

    private PreparedQuery<Postupak> makePostupciForVrsteParnica() throws SQLException {
        // build our inner query for UserPost objects
        QueryBuilder<PostupakVrstaParniceJoin, Integer> userPostQb = databaseHelper.getPostupakVrsteParnicaDao().queryBuilder();
        // just select the post-id field
        userPostQb.selectColumns(PostupakVrstaParniceJoin.POSTUPAK_ID);
        SelectArg userSelectArg = new SelectArg();
        // you could also just pass in user1 here
        userPostQb.where().eq(PostupakVrstaParniceJoin.VRSTA_PARNICE_ID, userSelectArg);

        // build our outer query for Post objects
        QueryBuilder<Postupak, Integer> postQb = databaseHelper.getPostupakDao().queryBuilder();
        // where the id matches in the post-id from the inner query
        postQb.where().in(Postupak.POSTUPAK_ID, userPostQb);
        return postQb.prepare();
    }

    private PreparedQuery<VrsteParnica> vrsteparnicaForPostupakkQuery = null;

    private List<VrsteParnica> lookUpPostupciForVrstaParnica(Postupak postupak) throws SQLException{
        if(vrsteparnicaForPostupakkQuery == null){
            vrsteparnicaForPostupakkQuery = makeVrsteparnicaForPostupak();
        }
        vrsteparnicaForPostupakkQuery.setArgumentHolderValue(0, postupak);
        return databaseHelper.getmVrsteParnicaDao().query(vrsteparnicaForPostupakkQuery);
    }

    private PreparedQuery<VrsteParnica> makeVrsteparnicaForPostupak() throws SQLException {
        // build our inner query for UserPost objects
        QueryBuilder<PostupakVrstaParniceJoin, Integer> userPostQb = databaseHelper.getPostupakVrsteParnicaDao().queryBuilder();
        // just select the post-id field
        userPostQb.selectColumns(PostupakVrstaParniceJoin.VRSTA_PARNICE_ID);
        SelectArg userSelectArg = new SelectArg();
        // you could also just pass in user1 here
        userPostQb.where().eq(PostupakVrstaParniceJoin.POSTUPAK_ID, userSelectArg);
        // build our outer query for Post objects
        QueryBuilder<VrsteParnica, Integer> postQb = databaseHelper.getmVrsteParnicaDao().queryBuilder();
        // where the id matches in the post-id from the inner query
        postQb.where().in(VrsteParnica.ID, userPostQb);
        return postQb.prepare();
    }

    /////////////////

    private void loadSpinnersTest()throws SQLException{

        //izlistavam dve vrste parnica procenjivu i neprocenjivu preko metode lookUpPostupciForVrstaParnica(postupak) i objekta klase Postupak
        //procenjena vrednost ima istu tabelu bodova za sve ali kod neprocenjene nije takav slucaj(VANPARNICA, UPRAVNI POSTUPAK, UPRAVNI SPOR I OSTALI POSTUPCI) imaju zasebne tabele bodova
        //zato proveravam ako je vrsteParnicaObjekat.getId()==2, 2 je sifra za neprocenjeno, zatim proveravam da li je jedan od ova cetri

        final List<VrsteParnica> vrsteParnicas = lookUpPostupciForVrstaParnica(postupak);
        ArrayAdapter<VrsteParnica> adapter = new ArrayAdapter<VrsteParnica>(AddParnicaActivity.this, android.R.layout.simple_spinner_item, vrsteParnicas);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerVrsteParnica.setAdapter(adapter);
        spinnerVrsteParnica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vrsteParnicaObjekat = (VrsteParnica) spinnerVrsteParnica.getSelectedItem();
//                    vrsteParnicaid = vrsteParnicaObjekat.getId();
                List<TabelaBodova> list = null;
                try {
                    //procenjiva vrednost je za sve ista ali neprocenjiva nije ista za svakog
                    //todo imam objekat klase postupak a njegov id imam kao foreign key u objektu tabelaBOdova
                    if(vrsteParnicaObjekat.getVrstaParnice()=="neprocenjiva"){
                        if(postupak.getNazivpostupka().equals("Vanparnicni postupak") || postupak.getNazivpostupka().equals("Upravni postupak") || postupak.getNazivpostupka().equals("Upravni sporovi") || postupak.getNazivpostupka().equals("Ostali postupci")) {
                            list = getDatabaseHelper().getmTabelaBodovaDao().queryBuilder()
                                    .where()
                                    .eq(TabelaBodova.VRSTE_PARNICA_ID, vrsteParnicaObjekat.getId())
                                    .and()
                                    .eq(TabelaBodova.POSTUPAK_ID, postupak.getId())
                                    .query();
                        }else {
                            list = getDatabaseHelper().getmTabelaBodovaDao().queryBuilder()
                                    .where()
                                    .eq(TabelaBodova.VRSTE_PARNICA_ID, vrsteParnicaObjekat.getId())
                                    .and()
                                    .isNull(TabelaBodova.POSTUPAK_ID)
                                    .query();
                        }

                        //todo add else if...
                    } else {
                        list = getDatabaseHelper().getmTabelaBodovaDao().queryBuilder()
                                .where()
                                .eq(TabelaBodova.VRSTE_PARNICA_ID, vrsteParnicaObjekat.getId())
                                .query();
                    }
                    ArrayAdapter<TabelaBodova> adapter1 = new ArrayAdapter(AddParnicaActivity.this, android.R.layout.simple_spinner_item, list);
                    adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinnerTabelaBodova.setAdapter(adapter1);
                    tabelaBodovaObj = (TabelaBodova) spinnerTabelaBodova.getSelectedItem();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        //todo dodavanje u db sifrua, brojStranaka, okrivljen, tabelaBodova, postupak

        Slucaj slucaj = new Slucaj();
        slucaj.setBroj_slucaja(Integer.valueOf(sifraSlucaja.getText().toString()));
        slucaj.setPostupak(postupak);
        slucaj.setTabelaBodova(tabelaBodovaObj);
        Log.d("Vrednost", String.valueOf(numOfClients.getValue()));
        slucaj.setBroj_stranaka(numOfClients.getValue());

        try {
                    getDatabaseHelper().getSlucajDao().create(slucaj);

                    Intent i = new Intent(AddParnicaActivity.this, PronadjeniSlucaj.class);
                    i.putExtra(FROM, "add");
                    i.putExtra(CASE_ID, slucaj.getId());
                    startActivity(i);

             Toast.makeText(AddParnicaActivity.this, "Novi je dodat\n"
                    +slucaj.getBroj_slucaja() + "\n"
                    +slucaj.getTabelaBodova() + "\n"
                    +slucaj.getPostupak() + "\n"
                    +slucaj.getBroj_stranaka(), Toast.LENGTH_LONG).show();


                }catch (SQLiteConstraintException e){
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();

            Toast.makeText(AddParnicaActivity.this, "Novi ne moze da se doda", Toast.LENGTH_LONG).show();
                }
    }
}
