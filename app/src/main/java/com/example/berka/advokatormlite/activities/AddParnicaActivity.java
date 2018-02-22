package com.example.berka.advokatormlite.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.example.berka.advokatormlite.model.PostupakVrstaParniceJoin;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.VrsteParnica;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by berka on 19-Oct-17.
 */

public class AddParnicaActivity extends AppCompatActivity implements IDynamicallyAddEditTexts{

    private DatabaseHelper databaseHelper;
    int postupak_id, broj_stranaka;

    GridLayout gridLayout;
    private StrankaDynamicViews strankaDynamicViews;
    private StrankaDetail strankaDetail;
    private List<StrankaDetail> strankaDetailsList;
    private Context context;

    public static final String FROM = "from";
    public static final String CASE_ID = "case_id";

    TabelaBodova tabelaBodovaObj;
    Postupak postupak;
    VrsteParnica vrsteParnicaObjekat;

    @BindView(R.id.btnAddSlucajParnica)
    Button btnAddSlucajParnica;

    int redni_broj_prvog_et_u_grid_ll = 2;

    EditText sifraSlucaja;
    Spinner spinnerVrsteParnica, spinnerTabelaBodova;
    ScrollableNumberPicker numOfClients;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parnica);

        initPostupak();
        addEditTextsProgramabilno();
        ButterKnife.bind(AddParnicaActivity.this);
        initWidgets();
        try {
            loadSpinnersTest();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initPostupak(){

        gridLayout = findViewById(R.id.parnica_gridlayout_za_dinamicko_dodavanje_polja);
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
    public ArrayList<StrankaDetail> allStrankeFromListViewToArrayList(){
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

    private void initWidgets(){

        spinnerVrsteParnica = (Spinner) findViewById(R.id.spinnerVrsteParnice);
        spinnerTabelaBodova = (Spinner) findViewById(R.id.spinnerKazne);
        btnAddSlucajParnica = (Button) findViewById(R.id.btnAddSlucajParnica);
        sifraSlucaja = (EditText) findViewById(R.id.sifraSlucajaParnica);
    }

    public StrankaDetail getValuesFromGridLayout(int position) {

        strankaDetail = new StrankaDetail();

        EditText imeprezime = (EditText) gridLayout.getChildAt(position+2);
        EditText adresa = (EditText) gridLayout.getChildAt(position+3);
        EditText mesto = (EditText) gridLayout.getChildAt(position+4);

        strankaDetail.setIme_i_prezime(imeprezime.getText().toString());
        strankaDetail.setAdresa(adresa.getText().toString());
        strankaDetail.setMesto(mesto.getText().toString());

        Log.d("Velicina", String.valueOf(strankaDetail.getIme_i_prezime()));

        return strankaDetail;
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

        final List<VrsteParnica> vrsteParnicaLista = lookUpPostupciForVrstaParnica(postupak);
        ArrayAdapter<VrsteParnica> adapter = new ArrayAdapter<VrsteParnica>(AddParnicaActivity.this, android.R.layout.simple_spinner_item, vrsteParnicaLista);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerVrsteParnica.setAdapter(adapter);
        spinnerVrsteParnica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vrsteParnicaObjekat = (VrsteParnica) spinnerVrsteParnica.getSelectedItem();
                Log.d("TAG", "onItemSelected: " + vrsteParnicaObjekat.getId());
                Log.d("TAG", "onItemSelected: " + postupak.getId());
//                    vrsteParnicaid = vrsteParnicaObjekat.getId();
                List<TabelaBodova> tabelaBodovaList = null;
                try {
                    //procenjiva vrednost je za sve ista za sve vrste postupaka ali neprocenjiva nije ista za svako, ovih cetri vrednosti(vanparnica, uprava postupci, uprava sporovi, ostali) imaju unikatne vrednosti u DB u tabeli bodova.
                    //broj 2 = neprocenjivo
                    if(vrsteParnicaObjekat.getId() == 2){
                        if(postupak.getNazivpostupka().equals("Vanparnicni postupak") || postupak.getNazivpostupka().equals("Upravni postupak") || postupak.getNazivpostupka().equals("Upravni sporovi") || postupak.getNazivpostupka().equals("Ostali postupci")) {
                            tabelaBodovaList = getDatabaseHelper().getmTabelaBodovaDao().queryBuilder()
                                    .where()
                                    .eq(TabelaBodova.VRSTE_PARNICA_ID, vrsteParnicaObjekat.getId())
                                    .and()
                                    .eq(TabelaBodova.POSTUPAK_ID, postupak.getId())
                                    .query();
                        }else {
                            tabelaBodovaList = getDatabaseHelper().getmTabelaBodovaDao().queryBuilder()
                                    .where()
                                    .eq(TabelaBodova.VRSTE_PARNICA_ID, vrsteParnicaObjekat.getId())
                                    .and()
                                    .isNull(TabelaBodova.POSTUPAK_ID)
                                    .query();
                        }

                        //todo add else if...
                    } else {
                        tabelaBodovaList = getDatabaseHelper().getmTabelaBodovaDao().queryBuilder()
                                .where()
                                .eq(TabelaBodova.VRSTE_PARNICA_ID, vrsteParnicaObjekat.getId())
                                .query();
                    }
                    ArrayAdapter<TabelaBodova> adapter1 = new ArrayAdapter(AddParnicaActivity.this, android.R.layout.simple_spinner_item, tabelaBodovaList);
                    adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinnerTabelaBodova.setAdapter(adapter1);

                    spinnerTabelaBodova.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            tabelaBodovaObj = (TabelaBodova) spinnerTabelaBodova.getSelectedItem();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                   // tabelaBodovaObj = (TabelaBodova) spinnerTabelaBodova.getSelectedItem();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.btnAddSlucajParnica)
    public void addSlucaj() {

        //todo dodavanje u db sifrua, brojStranaka, okrivljen, tabelaBodova, postupak
        Slucaj slucaj = new Slucaj();

        try {
            if (sifraSlucaja.getText().toString() == null || sifraSlucaja.getText().toString().trim().equals("")) {
                Toast.makeText(AddParnicaActivity.this, "Morate uneti sifru slucaja", Toast.LENGTH_LONG).show();
            } else {

                slucaj.setBroj_slucaja(Integer.valueOf(sifraSlucaja.getText().toString()));
                slucaj.setPostupak(postupak);
                slucaj.setTabelaBodova(tabelaBodovaObj);
                slucaj.setBroj_stranaka(broj_stranaka);
            }
        }catch (NumberFormatException e){
            Toast.makeText(this, "Za testiranje dozvoljen unos samo brojeva", Toast.LENGTH_LONG).show();
        }

        try {
            getDatabaseHelper().getSlucajDao().create(slucaj);

            Intent intent = new Intent(AddParnicaActivity.this, PronadjeniSlucaj.class);
            intent.putExtra(FROM, "add");
            intent.putExtra(CASE_ID, slucaj.getId());
            startActivity(intent);

            Toast.makeText(AddParnicaActivity.this, "Novi je dodat\n"
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
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("tag", "ne moz eda s doda");
            Toast.makeText(AddParnicaActivity.this, "Novi ne moze da se doda", Toast.LENGTH_LONG).show();
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
