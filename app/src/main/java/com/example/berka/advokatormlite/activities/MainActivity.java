package com.example.berka.advokatormlite.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    public static String POSTUPAK_KEY = "POSTUPAK_KEY";
    public static String FIND_KEY = "FIND_KEY";
    Button btn_find, btn_add, btn_edit;


    String str_broj_slucaja;
    int int_broj_slucaja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDialogFind();

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDialogAdd();
            }
        });



    }

    //tri metoda pomocu kojih otvaram dijaloge za add, find, edit

    private void openDialogFind(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_find_case);



        final EditText dialog_et_broj_slucaja = (EditText) dialog.findViewById(R.id.dialog_et_broj_slucaja);
        Button dialog_btn_find_case = (Button) dialog.findViewById(R.id.dialog_btn_nadji_slucaj);

        dialog_btn_find_case.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {

                    //todo ovde treba verovatno lista pa da se uzme prvi iz te liste
                    //stavljam u try/catch zbog mogucnosti da se ne unese broj
                    //pretvaram string u integer
                    try{
                        str_broj_slucaja = dialog_et_broj_slucaja.getText().toString();
                        int_broj_slucaja = Integer.parseInt(str_broj_slucaja);
                        Log.d("Provera", str_broj_slucaja);
                    }catch(NumberFormatException ex){
                        ex.printStackTrace();
                    }
                    QueryBuilder<Slucaj, Integer> qb = getDatabaseHelper().getSlucajDao().queryBuilder();
                    qb.where().eq(Slucaj.BROJ_SLUCAJA, int_broj_slucaja);

                    List<Slucaj> slucajevi = qb.query();

                    //sad treba uzeti taj objekat i otvoriti vrednosti proces/tarifa/radnja, samo prvi put treba da izabere proces posalji idPostupka iz tabele slucaja

                    //todo tabela bodova nekako povezati

                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra(FIND_KEY, slucajevi.get(0).getId());
                    Log.d("ajdi", String.valueOf(slucajevi.get(0).getId()));
                    startActivity(intent);

                    Log.d("Bilder", slucajevi.get(0).getIme());

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });



        dialog.show();
    }

    private void openDialogAdd(){

        Intent i = new Intent(this, AddNewCaseActivity.class);
        startActivity(i);
    }

    private void openDialogEdit(Radnja radnja){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_points);

        TextView tv_broj_bodova = dialog.findViewById(R.id.box_tv_points);
        tv_broj_bodova.setText(radnja.getNaziv_radnje());

        dialog.show();
    }

    private void initDialogVidgets(){


    }

    private void initWidgets(){

        btn_find = (Button) findViewById(R.id.btn_find_case);
        btn_add = (Button) findViewById(R.id.welcome_btn_add_case);
        btn_edit = (Button) findViewById(R.id.btn_edit_case);
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