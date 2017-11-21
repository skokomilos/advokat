package com.example.berka.advokatormlite.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends BaseActivity {

    private DatabaseHelper databaseHelper;
    public static final String FROM = "from";
    public static final String CASE_ID = "case_id";
    Button btn_find, btn_add, btn_edit;


    String str_broj_slucaja;
    int int_broj_slucaja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
        setupToolbar();

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
                    str_broj_slucaja = dialog_et_broj_slucaja.getText().toString();
                    QueryBuilder<Slucaj, Integer> qb = getDatabaseHelper().getSlucajDao().queryBuilder();
                    qb.where().eq(Slucaj.BROJ_SLUCAJA, Integer.parseInt(str_broj_slucaja));

                    try {
                        List<Slucaj> slucajevi = qb.query();

                        Intent intent = new Intent(MainActivity.this, PronadjeniSlucaj.class);
                        intent.putExtra(FROM, "find");
                        intent.putExtra(CASE_ID, slucajevi.get(0).getId());
                        startActivity(intent);
                    }catch (IndexOutOfBoundsException e){
                        Toast.makeText(MainActivity.this, "Ne postoji slucaj", Toast.LENGTH_SHORT).show();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDialogAdd(){

        Intent i = new Intent(this, ChoosePostupakActivity.class);
        startActivity(i);
    }

    private void initWidgets(){

        btn_find = (Button) findViewById(R.id.btn_find_case);
        btn_add = (Button) findViewById(R.id.welcome_btn_add_case);
        btn_edit = (Button) findViewById(R.id.btn_edit_case);
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