package com.example.berka.advokatormlite.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.NadjiSlucajActivity;
import com.example.berka.advokatormlite.adapter.ExpandableAdapterRadnja;
import com.example.berka.advokatormlite.adapter.MyAdapteAddEditStranke;
import com.example.berka.advokatormlite.adapter.MyAdapterSveStranke;
import com.example.berka.advokatormlite.adapter.MyAdapterSviSlucajevi;
import com.example.berka.advokatormlite.adapter.MyResultAdapter;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.IzracunatTrosakRadnje;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.PostupakTarifaJoin;
import com.example.berka.advokatormlite.model.PostupakVrstaParniceJoin;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.Tarifa;
import com.example.berka.advokatormlite.model.VrsteParnica;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by berka on 7/21/2017.
 */

public class PronadjeniSlucaj extends BaseActivity {


    private ExpandableListView exlistView;
    private ExpandableListAdapter exlistAdapter;
    private List<Tarifa> listDataHeader;
    private HashMap<Tarifa, List<Radnja>> listHashMap;

    public  static  final String SLUCAJ_KEY = "slucaj_key";


    DatabaseHelper databaseHelper;
    Postupak postupak;
    Radnja radnja;
    Slucaj slucaj;

    private String comingfrom;

    private int caseid, case_add, caseid_find, caseid_all;


    List<StrankaDetail> detailStrankaList;

    //menja se svaki put kad promenim radnju(tarifa/radnja)
    private double privremenaCena;

    private double vrednostJednogBodaUDinarima = 30;

    private ListView listViewStrankeAddEdit;
    int redni_broj_prvog_et_u_dialogu = 1;

    MyAdapteAddEditStranke myadaper;

    //TODO iz main activitija dobijam broj pomocu koga trazim slucaj iz baze, treba pronaci id_slucaja i uzeti fiksni broj bodova, slucaj.getBrojBodova i dodeliti ga nekoj globalnoj promenljivoj koju cu zatim koristiti u svakom metodu iz switch petlje


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        exlistView = (ExpandableListView) findViewById(R.id.lvExpendable);
        listViewStrankeAddEdit = findViewById(R.id.lista_add_edit_stranke);
        checkIntent();

        if(postupak.getNazivpostupka().equals("Krivicni postupak")){
            Log.d(String.valueOf(getLocalClassName()), "funkcionise");
            try {
                initDataFromKrivica();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            try {
                initDataFromOthersActivities();
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

        exlistAdapter = new ExpandableAdapterRadnja(this, listDataHeader, listHashMap);
        exlistView.setAdapter(exlistAdapter);

        exlistView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {

             /* You must make use of the View v, find the view by id and extract the text as below*/
                //Log.d("Broj", listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition).getNaziv_radnje().toString());
                radnja = listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);

                //ovde pitati da li je slucaj KRIVICA i da li je OKRIVLJEN == 0 (0=true) ona cetri pravila
                //todo okrivljen, ostecen, okrivljen vise, ostecen vise

                if( radnja != null){

                    //Za radnju koja ce nositi celu vrednost bodova sifra biti 1, za radnju koja ce nositi vrednost umanjenu za 50% sifra 2
                    //ovde u zavistosti na vrednost te sifre radnja.getSifra(), switch petlja i podmetode za racunjanje
                    //kad izracuna doda tu vrednost u listu radnji u klasi slucaj
                    //sifra 1: puna vrednost, 2: pola, 3: duplo, 4: vrednost i 50 bodova po satu, 5: puna vrednost i broj sati moze biti i pola ako se ne odrzi
                    switch (radnja.getSifra()){

                        case 1:
                            //metoda koja racuna pravu vrednost i ima sifru 1
                            Toast.makeText(PronadjeniSlucaj.this,"Sifra ove radnje je " + String.valueOf(radnja.getSifra()) + " radnja ima fiksnu vrednost", Toast.LENGTH_SHORT).show();
                            privremenaCena = wholeValue(slucaj.getBroj_stranaka());
                            openDialog(privremenaCena, radnja.getNaziv_radnje());
                            break;
                        case 2:
                            //metoda koja racuna polovinu vrednosti ima sifru 2
                            Toast.makeText(PronadjeniSlucaj.this,"Sifra ove radnje je " +  String.valueOf(radnja.getSifra()) + " radnja ima polovinu vrednosti", Toast.LENGTH_SHORT).show();
                            privremenaCena = halfValue(slucaj.getBroj_stranaka());
                            openDialog(privremenaCena, radnja.getNaziv_radnje());
                            break;
                        case 3:
                            //metoda koja racuna duplu vrednost ima sifru 3
                            Toast.makeText(PronadjeniSlucaj.this,"Sifra ove radnje je " +  String.valueOf(radnja.getSifra() + " radnja ima dupliranu vrednost"), Toast.LENGTH_SHORT).show();
                            privremenaCena = doubleValue(slucaj.getBroj_stranaka());
                            openDialog(privremenaCena, radnja.getNaziv_radnje());
                            break;
                        case 4:
                            //metoda koja racuna pravu vrednost plus broj sati ima sifru 4
                            Toast.makeText(PronadjeniSlucaj.this, "Sifra ove radnje je " +  String.valueOf(radnja.getSifra()), Toast.LENGTH_SHORT).show();
                            privremenaCena = wholePlusHours(slucaj.getBroj_stranaka());
                            dialogFixValuePlusHours(privremenaCena);
                            break;
                        default:
                    }
                }

                return true;  // i missed this
            }
        });
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
            case"all":
                caseid_all = caseid;
                addIdToSlucaj(caseid_all);
                break;
            default:
        }
    }

    private void addIdToSlucaj(int caseid){
        try {
            Log.d("id slucaja : ", String.valueOf(caseid));
            slucaj = getDatabaseHelper().getSlucajDao().queryForId(caseid);
            Log.d("Sifra slucaja : ", String.valueOf(slucaj.getBroj_slucaja()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        postupak = slucaj.getPostupak();
        Log.d("Naziv postupka je : ", postupak.getNazivpostupka());
    }

    private void prikaziSveStranke(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PronadjeniSlucaj.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_sve_stranke, null);
        mBuilder.setTitle("Sve stranke ovog slucaja: ");
        ListView listView = mView.findViewById(R.id.lista_sve_stranke);

        try {
            detailStrankaList = getDatabaseHelper().getmStrankaDetail().queryBuilder()
            .where()
            .eq(StrankaDetail.ID_SLUCAJA, slucaj.getId())
            .query();

            MyAdapterSveStranke myadaper = new MyAdapterSveStranke(PronadjeniSlucaj.this, (ArrayList<StrankaDetail>) detailStrankaList);
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

    private void dodajIzmeniStranke(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PronadjeniSlucaj.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_edit_stranke, null);
        mBuilder.setTitle("Dodaj ili izmeni vec postojece podatke o strankama");
        ListView listView = mView.findViewById(R.id.lista_add_edit_stranke);

        try {
            detailStrankaList = getDatabaseHelper().getmStrankaDetail().queryBuilder()
                    .where()
                    .eq(StrankaDetail.ID_SLUCAJA, slucaj.getId())
                    .query();

            myadaper = new MyAdapteAddEditStranke(PronadjeniSlucaj.this, (ArrayList<StrankaDetail>) detailStrankaList);
            listView.setAdapter(myadaper);
            myadaper.notifyDataSetChanged();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        mBuilder.setPositiveButton("Potvrdi", new DialogInterface.OnClickListener()  {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                myadaper.notifyDataSetChanged();

                Log.d("majstor", detailStrankaList.get(0).getIme_i_prezime());
                for (int j = 0; j < detailStrankaList.size(); j++) {
                    try {
                        getDatabaseHelper().getmStrankaDetail().update(detailStrankaList.get(j));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Izadji")
                .setMessage("Pritiskom na ok izlazite na glavni meni")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                    Intent i=new Intent(PronadjeniSlucaj.this,MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }).create().show();
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
        getMenuInflater().inflate(R.menu.lista_stranaka_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
            case R.id.action_show_stranke:
                prikaziSveStranke();
                Toast.makeText(PronadjeniSlucaj.this, "Good job", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_edit_stranke:
                dodajIzmeniStranke();
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
        double izrecunataVrednostUBodovima = cena + ((cena/2) * (brojStranaka -1));
        return izrecunataVrednostUBodovima * vrednostJednogBodaUDinarima;
    }

    private double halfValue(int brojStranaka){

        double cena = Double.valueOf(slucaj.getTabelaBodova().getBodovi());
        double izrecunataVrednostUBodovima = (cena + ((cena/2) * (brojStranaka -1))) / 2;
        return izrecunataVrednostUBodovima * vrednostJednogBodaUDinarima;
    }

    private double doubleValue(int brojStranaka){

        double cena = Double.valueOf(slucaj.getTabelaBodova().getBodovi());
        double izrecunataVrednostUBodovima = (cena + ((cena/2) * (brojStranaka -1))) * 2;
        return izrecunataVrednostUBodovima * vrednostJednogBodaUDinarima;
    }

    private double wholePlusHours(int brojStranaka){

        int cena = slucaj.getTabelaBodova().getBodovi();
        double izrecunataVrednostUBodovima = cena + ((cena/2) * (brojStranaka -1));
        return izrecunataVrednostUBodovima * vrednostJednogBodaUDinarima;
    }

    private void openDialog(double num, String imeRadnje){

        new AlertDialog.Builder(this)
        .setTitle(imeRadnje)
        .setMessage("Vrednost ove radnje iznosi: " +  String.valueOf(num) + " dinara" +  "\n\n" + "Da li zelite da je dodate?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {

                    IzracunatTrosakRadnje izracunatTrosakRadnje = new IzracunatTrosakRadnje();
                    izracunatTrosakRadnje.setDatum(getNowDate());
                    izracunatTrosakRadnje.setCena_izracunate_jedinstvene_radnje(num);
                    izracunatTrosakRadnje.setNaziv_radnje(radnja.getNaziv_radnje());
                    izracunatTrosakRadnje.setSlucaj(slucaj);

                    try {
                        getDatabaseHelper().getmIzracunatTrosakRadnjeDao().create(izracunatTrosakRadnje);
                        Toast.makeText(PronadjeniSlucaj.this, izracunatTrosakRadnje.getDatum() +  " Izracunata radnja je dodata", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Toast.makeText(PronadjeniSlucaj.this, "Izracunata radnja nije dodata", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
    }

    private String getNowDate(){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String datestring = sdf.format(c.getTime());
        return datestring;
    }

    private void dialogFixValuePlusHours(final double cena){

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

                Log.d("Selektovani", String.valueOf(selecteid));
                switch (selecteid){

                    case R.id.radioButtonYes:
                        izracunato = cena + (scrollableNumberPicker.getValue()*50);
                        Toast.makeText(PronadjeniSlucaj.this, String.valueOf(izracunato), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioButtonNo:
                        izracunato = cena / 2 + (scrollableNumberPicker.getValue()*50);
                        Toast.makeText(PronadjeniSlucaj.this, String.valueOf(izracunato), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(PronadjeniSlucaj.this, "Oznaci da li je radnja odrzana", Toast.LENGTH_LONG).show();
                        //todo resiti problem kad cekiram NE da ne dozvoli koriscenje scrolablenumberpickera
                }

                IzracunatTrosakRadnje izracunatTrosakRadnje = new IzracunatTrosakRadnje();
                izracunatTrosakRadnje.setDatum(getNowDate());
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
                dialog.dismiss();
            }
        });
                dialog.show();
    }

    //TODO ODAVDE METODE VISE NA VISE
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

    private void initDataFromKrivica()throws SQLException{

        //checkIntent();
        listHashMap = new HashMap<>();
        List<Radnja> listaRadnji = new ArrayList<>();

        try {
            listDataHeader = getDatabaseHelper().getmTarifaDao().queryBuilder()
                    .where()
                    .eq(Tarifa.FIELD_TARIFA_POSTUPAK, postupak.getId())
                    .query();

            Log.d("FromKrivica", String.valueOf(listDataHeader.size()));
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

    private void initDataFromOthersActivities() throws SQLException{
        listHashMap = new HashMap<>();
        List<Radnja> radnje = new ArrayList<>();

        //checkIntent();
        listDataHeader = lookUpTarifeForPostupak(postupak);
        Log.d("listaradnji", String.valueOf(postupak.getNazivpostupka()));

            for (int i = 0; i < listDataHeader.size(); i++) {

            Log.d("Provera", String.valueOf(listDataHeader.size()));

                //Log.d("Greska", showShowShow.get(0).getNaziv_radnje());
                //listaRadnji = new ArrayList<>(listDataHeader.get(i).getRadnje());
                try {
                    radnje = getDatabaseHelper().getmRadnjaDao().queryBuilder()
                            .where()
                            .eq(Radnja.FIELD_RADNJA_TARIFA, listDataHeader.get(i).getId())
                            .and()
                            .eq(Radnja.FIELD_SIFRA_POSTUPKA, postupak.getId())
                            .query();
                    Log.d("ListaRadnji", String.valueOf(radnje.size()));
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.d("Liste tarifa i radnji", "Ne mogu da se dodaju");
                }
                listHashMap.put(listDataHeader.get(i), radnje);
        }
    }

    public DatabaseHelper getDatabaseHelper(){
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(this);
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
