package com.example.berka.advokatormlite.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.NadjiSlucajActivity;
import com.example.berka.advokatormlite.adapter.ExpandableAdapterRadnja;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.IzracunatTrosakRadnje;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.Tarifa;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by berka on 7/21/2017.
 */

public class SecondActivity extends AppCompatActivity {

    
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private ArrayList<Tarifa> listDataHeader;
    private HashMap<Tarifa, List<Radnja>> listHashMap;

    public  static  final String SLUCAJ_KEY = "slucaj_key";


    DatabaseHelper databaseHelper;
    Postupak postupak;
    Radnja radnja;
    Slucaj slucaj;
    int key;

    //menja se svaki put kad promenim radnju(tarifa/radnja)
    private double privremenaCena;

    //TODO iz main activitija dobijam broj pomocu koga trazim slucaj iz baze, treba pronaci id_slucaja i uzeti fiksni broj bodova, slucaj.getBrojBodova i dodeliti ga nekoj globalnoj promenljivoj koju cu zatim koristiti u svakom metodu iz switch petlje


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);



        listView = (ExpandableListView) findViewById(R.id.lvExpendable);
        initData();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSecond);

        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(slucaj.getIme());
        }

        Button izracunaj = (Button) findViewById(R.id.izracunaj_vrednost);
        izracunaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SecondActivity.this, KonacniTrosakSvihRadnjiActivity.class);
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


                //listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition).getNaziv_radnje().toString();

                radnja = listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);


                if( radnja != null){

                    //todo dodati u tabelu radnji sifru za svaku radnju tako da znam kako da racunam bodove
                    //tako da ce za radnju koja ce nositi celu vrednost bodova sifra biti 1, za radnju koja ce nositi vrednost umanjenu za 50p sifra 2
                    //ovde u zavistosti na vrednost te sifre radnja.getSifra(), switch petlja i podmetode za racunjanje
                    //kad izracuna doda tu vrednost u listu radnji u klasi slucaj
                    //sifra 1: puna vrednost, 2: pola, 3: duplo, 4: vrednost i 50 bodova po satu


                    switch (radnja.getSifra()){

                        case 1:
                            //metoda koja racuna pravu vrednost
                            Toast.makeText(SecondActivity.this,"Sifra ove radnje je " + String.valueOf(radnja.getSifra()) + " radnja ima fiksnu vrednost", Toast.LENGTH_SHORT).show();
                            privremenaCena = wholeValue();
                            break;
                        case 2:
                            //metoda koja racuna polovinu vrednosti
                            Toast.makeText(SecondActivity.this,"Sifra ove radnje je " +  String.valueOf(radnja.getSifra()) + " radnja ima polovinu vrednosti", Toast.LENGTH_SHORT).show();
                            privremenaCena = halfValue();
                            break;
                        case 3:
                            //metoda koja racuna duplu vrednost
                            Toast.makeText(SecondActivity.this,"Sifra ove radnje je " +  String.valueOf(radnja.getSifra() + " radnja ima dupliranu vrednost"), Toast.LENGTH_SHORT).show();
                            privremenaCena = doubleValue();
                            break;
                        case 4:
                            //metoda koja racuna pravu vrednost plus broj sati
                            Toast.makeText(SecondActivity.this, "Sifra ove radnje je " +  String.valueOf(radnja.getSifra()), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                    }



                    Log.d("Broj", listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition).getNaziv_radnje().toString());
                    Log.d("Uslov", radnja.getNaziv_radnje());

                    openDialog(privremenaCena);
                }



                return true;  // i missed this
            }
        });

    }


    //metode koje racunaju bodove i smestaju ih u bazu tj tabelu SLUCAJ kolona LISTA_BODOVA

    private double wholeValue(){

        double cena = Double.valueOf(slucaj.getTabelaBodova().getBodovi());
        return cena;
    }

    private double halfValue(){

        double cena = slucaj.getTabelaBodova().getBodovi()/2;
        return cena;
    }

    private double doubleValue(){
        double cena = Double.valueOf(slucaj.getTabelaBodova().getBodovi())*2;
        return cena;
    }

    private String wholePlusFifty(int num){
        int cena = slucaj.getTabelaBodova().getBodovi() + num;
        return String.valueOf(cena);
    }



    private void openDialog(final double num){

        final double cenaRadnje = num;


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_points);

        TextView tv_broj_bodova = dialog.findViewById(R.id.box_tv_points);
        tv_broj_bodova.setText(String.valueOf(cenaRadnje));

        Button button_yes = dialog.findViewById(R.id.box_btn_yes);
        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IzracunatTrosakRadnje izracunatTrosakRadnje = new IzracunatTrosakRadnje();
                izracunatTrosakRadnje.setCena_izracunate_jedinstvene_radnje(cenaRadnje);
                izracunatTrosakRadnje.setNaziv_radnje(radnja.getNaziv_radnje());
                izracunatTrosakRadnje.setSlucaj(slucaj);

                try {
                    getDatabaseHelper().getmIzracunatTrosakRadnjeDao().create(izracunatTrosakRadnje);
                    Toast.makeText(SecondActivity.this, "Izracunata radnja je dodata", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(SecondActivity.this, "Izracunata radnja nije dodata", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialog.show();
    }

    private void initData(){

        List<Radnja> listaRadnji ;
        listDataHeader = new ArrayList<>();
        listHashMap = new HashMap<>();

        key = getIntent().getExtras().getInt(MainActivity.FIND_KEY);
        try {
             slucaj = getDatabaseHelper().getSlucajDao().queryForId(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //try {
            postupak = slucaj.getPostupak();
            //postupak = getDatabaseHelper().getPostupakDao().queryForId(key);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }



        try {
            listDataHeader = (ArrayList<Tarifa>) getDatabaseHelper().getmTarifaDao().queryBuilder()
                    .where()
                    .eq(Tarifa.FIELD_TARIFA_POSTUPAK, postupak.getId())
                    .query();

            if(listDataHeader==null){
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < listDataHeader.size(); i++) {

            if(listDataHeader.get(i).getRadnje()==null){

                return;
            }else{

                listaRadnji = new ArrayList<>(listDataHeader.get(i).getRadnje());
                listHashMap.put(listDataHeader.get(i), listaRadnji);
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
