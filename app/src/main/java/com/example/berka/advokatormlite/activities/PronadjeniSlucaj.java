package com.example.berka.advokatormlite.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.NadjiSlucajActivity;
import com.example.berka.advokatormlite.adapter.ExpandableAdapterRadnja;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.IzracunatTrosakRadnje;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.PostupakTarifaJoin;
import com.example.berka.advokatormlite.model.PostupakVrstaParniceJoin;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.Tarifa;
import com.example.berka.advokatormlite.model.VrsteParnica;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by berka on 7/21/2017.
 */

public class PronadjeniSlucaj extends BaseActivity {


    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<Tarifa> listDataHeader;
    private HashMap<Tarifa, List<Radnja>> listHashMap;

    public  static  final String SLUCAJ_KEY = "slucaj_key";


    DatabaseHelper databaseHelper;
    Postupak postupak;
    Radnja radnja;
    Slucaj slucaj;

    private RadioButton radioButton;

    private String comingfrom;

    private int caseid;
    private int case_add;
    private int caseid_find;

    //menja se svaki put kad promenim radnju(tarifa/radnja)
    private double privremenaCena;

    //TODO iz main activitija dobijam broj pomocu koga trazim slucaj iz baze, treba pronaci id_slucaja i uzeti fiksni broj bodova, slucaj.getBrojBodova i dodeliti ga nekoj globalnoj promenljivoj koju cu zatim koristiti u svakom metodu iz switch petlje


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        listView = (ExpandableListView) findViewById(R.id.lvExpendable);

        checkIntent();

        if(postupak.getNazivpostupka().equals("Krivicni postupak")){
            Log.d("krivicni postupak", "funkcionise");
            try {
                initDataFromKrivica();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            try {
                initDataFromOthers();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        setupToolbar();


        Button izracunaj = (Button) findViewById(R.id.izracunaj_vrednost);
        izracunaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PronadjeniSlucaj.this, KonacniTrosakSvihRadnjiActivity.class);
                intent.putExtra(SLUCAJ_KEY, slucaj.getId());
                startActivity(intent);
            }
        });

        listAdapter = new ExpandableAdapterRadnja(this, listDataHeader, listHashMap);
        listView.setAdapter(listAdapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {

             /* You must make use of the View v, find the view by id and extract the text as below*/
                //Log.d("Broj", listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition).getNaziv_radnje().toString());
                radnja = listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);

                //ovde pitati da li je slucaj KRIVICA i da li je OKRIVLJEN == 0 (0=true) ona cetri pravila
                //todo okrivljen, ostecen, okrivljen vise, ostecen vise

                if( radnja != null){

                    //Za radnju koja ce nositi celu vrednost bodova sifra biti 1, za radnju koja ce nositi vrednost umanjenu za 50p sifra 2
                    //ovde u zavistosti na vrednost te sifre radnja.getSifra(), switch petlja i podmetode za racunjanje
                    //kad izracuna doda tu vrednost u listu radnji u klasi slucaj
                    //sifra 1: puna vrednost, 2: pola, 3: duplo, 4: vrednost i 50 bodova po satu, 5: puna vrednost i broj sati moze biti i pola ako se ne odrzi
                    switch (radnja.getSifra()){

                        case 1:
                            //metoda koja racuna pravu vrednost
                            Toast.makeText(PronadjeniSlucaj.this,"Sifra ove radnje je " + String.valueOf(radnja.getSifra()) + " radnja ima fiksnu vrednost", Toast.LENGTH_SHORT).show();
                            privremenaCena = wholeValue(slucaj.getBroj_stranaka());
                            openDialog(privremenaCena);
                            break;
                        case 2:
                            //metoda koja racuna polovinu vrednosti
                            Toast.makeText(PronadjeniSlucaj.this,"Sifra ove radnje je " +  String.valueOf(radnja.getSifra()) + " radnja ima polovinu vrednosti", Toast.LENGTH_SHORT).show();
                            privremenaCena = halfValue(slucaj.getBroj_stranaka());
                            openDialog(privremenaCena);
                            break;
                        case 3:
                            //metoda koja racuna duplu vrednost
                            Toast.makeText(PronadjeniSlucaj.this,"Sifra ove radnje je " +  String.valueOf(radnja.getSifra() + " radnja ima dupliranu vrednost"), Toast.LENGTH_SHORT).show();
                            privremenaCena = doubleValue(slucaj.getBroj_stranaka());
                            openDialog(privremenaCena);
                            break;
                        case 4:
                            //metoda koja racuna pravu vrednost plus broj sati
                            Toast.makeText(PronadjeniSlucaj.this, "Sifra ove radnje je " +  String.valueOf(radnja.getSifra()), Toast.LENGTH_SHORT).show();
                            privremenaCena = doubleValue(slucaj.getBroj_stranaka());
                            dialogFixValuePlusHours(privremenaCena);
                            break;
                        default:
                    }
                }

                return true;  // i missed this
            }
        });

    }

    //5 metoda za nav/toolbar
    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(slucaj.getPostupak().getNazivpostupka());
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

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //metode koje racunaju bodove i smestaju ih u bazu tj tabelu SLUCAJ kolona LISTA_BODOVA

    private double wholeValue(int brojStranaka){

        double cena = Double.valueOf(slucaj.getTabelaBodova().getBodovi());
        double izracunato = cena + ((cena/2) * (brojStranaka -1));
        return izracunato;
    }

    private double halfValue(int brojStranaka){

        double cena = Double.valueOf(slucaj.getTabelaBodova().getBodovi());
        double izracunato = (cena + ((cena/2) * (brojStranaka -1))) / 2;
        return izracunato;
    }

    private double doubleValue(int brojStranaka){

        double cena = Double.valueOf(slucaj.getTabelaBodova().getBodovi());
        double izracunato = (cena + ((cena/2) * (brojStranaka -1))) * 2;
        return izracunato;
    }

    private double wholePlusHours(int brojStranaka, int brojSati){

        int cena = slucaj.getTabelaBodova().getBodovi();
        double izracunato = (cena + ((cena/2) * (brojStranaka -1))) + (brojSati * 50);
        return izracunato;
    }

    private void openDialog(final double num){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_points);

        //final double cenaRadnje = num;
        TextView tv_broj_bodova = dialog.findViewById(R.id.box_tv_points);
        tv_broj_bodova.setText(String.valueOf(num));

        Button button_yes = dialog.findViewById(R.id.box_btn_yes);
        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IzracunatTrosakRadnje izracunatTrosakRadnje = new IzracunatTrosakRadnje();
                izracunatTrosakRadnje.setCena_izracunate_jedinstvene_radnje(num);
                izracunatTrosakRadnje.setNaziv_radnje(radnja.getNaziv_radnje());
                izracunatTrosakRadnje.setSlucaj(slucaj);

                try {
                    getDatabaseHelper().getmIzracunatTrosakRadnjeDao().create(izracunatTrosakRadnje);
                    Toast.makeText(PronadjeniSlucaj.this, "Izracunata radnja je dodata", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(PronadjeniSlucaj.this, "Izracunata radnja nije dodata", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
                dialog.show();

    }

    private void dialogFixValuePlusHours(final double num){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_fix_plus_hours);

        final Button no = dialog.findViewById(R.id.fix_hours_box_btn_no);

        final RadioGroup radioGroup = dialog.findViewById(R.id.radioGroupFixHours);
        radioGroup.check(R.id.radioButtonYes);
        final ScrollableNumberPicker scrollableNumberPicker = dialog.findViewById(R.id.snp_dialog_fix_plus_hours);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if(checkedId==R.id.radioButtonNo) {
                    scrollableNumberPicker.setEnabled(false);
                }else if(checkedId == R.id.radioButtonYes){
                    scrollableNumberPicker.setVisibility(View.VISIBLE);
                }
            }
        });
        Button btnYes = dialog.findViewById(R.id.fix_hours_box_btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double izracunato = 0;
                final int selecteid = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selecteid);

                Log.d("Selektovani", String.valueOf(selecteid));
                switch (selecteid){

                    case R.id.radioButtonYes:
                        izracunato = num + (scrollableNumberPicker.getValue()*50);
                        Toast.makeText(PronadjeniSlucaj.this, String.valueOf(izracunato), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioButtonNo:
                        izracunato = num / 2 + (scrollableNumberPicker.getValue()*50);
                        Toast.makeText(PronadjeniSlucaj.this, String.valueOf(izracunato), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(PronadjeniSlucaj.this, "Oznaci da li je radnja odrzana", Toast.LENGTH_LONG).show();
                        //todo resiti problem kad cekiram NE da ne dozvoli koriscenje scrolablenumberpickera
                }

                IzracunatTrosakRadnje izracunatTrosakRadnje = new IzracunatTrosakRadnje();
                izracunatTrosakRadnje.setCena_izracunate_jedinstvene_radnje(izracunato);
                izracunatTrosakRadnje.setNaziv_radnje(radnja.getNaziv_radnje());
                izracunatTrosakRadnje.setSlucaj(slucaj);

                try {
                    getDatabaseHelper().getmIzracunatTrosakRadnjeDao().create(izracunatTrosakRadnje);
                    Toast.makeText(PronadjeniSlucaj.this, "Izracunata radnja je dodata", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(PronadjeniSlucaj.this, "Izracunata radnja nije dodata", Toast.LENGTH_SHORT).show();
                }
                //todo resito dodavanje u bazu
                dialog.dismiss();

            }
        });

                dialog.show();
    }

    //TODO ODAVDE METODE VISE NA VISE I DA RESIM ISPIS U INITDATA()
    //metode many to many  https://github.com/j256/ormlite-jdbc/tree/master/src/test/java/com/j256/ormlite/examples/manytomany


    private PreparedQuery<Postupak> postupciForTarifaQuery = null;

    private List<Postupak> lookUpPostupciForTarifa(Tarifa tarifa) throws SQLException{
        if(postupciForTarifaQuery == null){
            postupciForTarifaQuery = makePostupciForTarifa();
        }
        postupciForTarifaQuery.setArgumentHolderValue(0, tarifa);
        return databaseHelper.getPostupakDao().query(postupciForTarifaQuery);
    }

    private PreparedQuery<Postupak> makePostupciForTarifa() throws SQLException{

        // build our inner query for UserPost objects
        QueryBuilder<PostupakTarifaJoin, Integer> userPostQb = databaseHelper.getmPostupakTarifaDao().queryBuilder();
        // just select the post-id field
        userPostQb.selectColumns(PostupakTarifaJoin.POSTUPAK_ID);
        SelectArg userSelectArg = new SelectArg();
        // you could also just pass in user1 here
        userPostQb.where().eq(PostupakTarifaJoin.TARIFA_ID, userSelectArg);

        // build our outer query for Post objects
        QueryBuilder<Postupak, Integer> postQb = databaseHelper.getPostupakDao().queryBuilder();
        // where the id matches in the post-id from the inner query
        postQb.where().in(Postupak.POSTUPAK_ID, userPostQb);
        return postQb.prepare();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    private PreparedQuery<Tarifa> tarifeForPostupakQuery = null;

    private List<Tarifa> lookUpTarifeForPostupak(Postupak postupak) throws SQLException{

        if(tarifeForPostupakQuery == null){
            tarifeForPostupakQuery = makeTarifeForPostupak();
        }
        tarifeForPostupakQuery.setArgumentHolderValue(0, postupak);
        return databaseHelper.getmTarifaDao().query(tarifeForPostupakQuery);

    }

    private PreparedQuery<Tarifa> makeTarifeForPostupak() throws SQLException{

        // build our inner query for UserPost objects
        QueryBuilder<PostupakTarifaJoin, Integer> userPostQb = databaseHelper.getmPostupakTarifaDao().queryBuilder();
        // just select the post-id field
        userPostQb.selectColumns(PostupakTarifaJoin.TARIFA_ID);
        SelectArg userSelectArg = new SelectArg();
        // you could also just pass in user1 here
        userPostQb.where().eq(PostupakTarifaJoin.POSTUPAK_ID, userSelectArg);
        // build our outer query for Post objects
        QueryBuilder<Tarifa, Integer> postQb = databaseHelper.getmTarifaDao().queryBuilder();
        // where the id matches in the post-id from the inner query
        postQb.where().in(Tarifa.FIELD_TARIFA_ID, userPostQb);
        return postQb.prepare();
    }

    private void checkIntent(){

        comingfrom=getIntent().getStringExtra("from");
        caseid=getIntent().getIntExtra("case_id",0);

        switch (comingfrom){
            case "add":
                case_add = caseid;
                addIdToSlucaj(case_add);
                break;
            case "find":
                caseid_find = caseid;
                addIdToSlucaj(caseid_find);
                break;
            default:
        }
    }

    private void addIdToSlucaj(int caseid){
        try {
            slucaj = getDatabaseHelper().getSlucajDao().queryForId(caseid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        postupak = slucaj.getPostupak();
        Log.d("tresnja", postupak.getNazivpostupka());
    }

    //TODO DOVDE IDE NOVI DEO

    private void initDataFromKrivica()throws SQLException{

        //checkIntent();
        listHashMap = new HashMap<>();
        List<Radnja> listaRadnji = new ArrayList<>();

        try {
            listDataHeader = getDatabaseHelper().getmTarifaDao().queryBuilder()
                    .where()
                    .eq(Tarifa.FIELD_TARIFA_POSTUPAK, postupak.getId())
                    .query();

            Log.d("ListaTarifa", String.valueOf(listDataHeader.size()));
            if(listDataHeader==null){
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < listDataHeader.size(); i++) {

            if (listDataHeader.get(i).getRadnje() == null) {

                return;
            } else {
                listaRadnji = new ArrayList<>(listDataHeader.get(i).getRadnje());
                listHashMap.put(listDataHeader.get(i), listaRadnji);
                Log.d("listaradnji", "uspesno dodato");
            }
        }
    }

    private void initDataFromOthers() throws SQLException{
        listHashMap = new HashMap<>();
        List<Radnja> showShowShow = new ArrayList<>();

        //checkIntent();
        listDataHeader = lookUpTarifeForPostupak(postupak);

            for (int i = 0; i < listDataHeader.size(); i++) {

            Log.d("Provera", String.valueOf(listDataHeader.get(i).getRadnje().size()));
            if(showShowShow==null){
                Log.d("Knjiga", "Greska");
                return;
            }else{
                //Log.d("Greska", showShowShow.get(0).getNaziv_radnje());
                //listaRadnji = new ArrayList<>(listDataHeader.get(i).getRadnje());
                try {
                    showShowShow = getDatabaseHelper().getmRadnjaDao().queryBuilder()
                            .where()
                            .eq(Radnja.FIELD_RADNJA_TARIFA, listDataHeader.get(i).getId())
                            .and()
                            .eq(Radnja.FIELD_SIFRA_POSTUPKA, postupak.getId())
                            .query();
                    Log.d("ListaRadnji", String.valueOf(showShowShow.size()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                listHashMap.put(listDataHeader.get(i), showShowShow);
            }
        }
    }

    public DatabaseHelper getDatabaseHelper(){
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazom podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
