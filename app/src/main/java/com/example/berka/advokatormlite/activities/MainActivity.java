package com.example.berka.advokatormlite.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.adapter.MyAdapterSviSlucajevi;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.IzracunatTrosakRadnje;
import com.example.berka.advokatormlite.model.Postupak;
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

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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

    //permission
    public static final int REQUEST_CODE_WRITE_STORAGE = 123;
    public static boolean WRITE_STORAGE_GRANTED = false;
    public static final String TAG = "Ljog";


    public static final String FROM_MAIN = "from_main";
    public static final String CASE_TYPE = "case_type";


    @BindView(R.id.cv_find)
    CardView cv_find;

    @BindView(R.id.cv_add)
    CardView cv_add;

    @BindView(R.id.cv_all)
    CardView cv_all;

    @BindView(R.id.cv_my_profile)
    CardView cv_my_profile;

    @BindView(R.id.cv_isprave)
    CardView cv_isprave;

    private List<Slucaj> slucajevi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_test);


        isWriteStorageGranted();
        setupToolbar();
        ButterKnife.bind(MainActivity.this);

    }


    public boolean isWriteStorageGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasWriteStoragePermission = ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE);
            if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
                Log.d("Ljog", "permission granted");
                return WRITE_STORAGE_GRANTED = true;
            } else {

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Camera permission necessary");
                alertBuilder.setMessage("FITsociety need camera permission to read barcode.");
                alertBuilder.setPositiveButton("next", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_STORAGE);

                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
                return WRITE_STORAGE_GRANTED = false;
            }
        } else {
            //permission is automatically granted on sdk<23 upon instalation
            Log.d("Ljog", "permission granted ako je uredjaj ispod 23sdk");
            return WRITE_STORAGE_GRANTED = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d("Mjetod", "onRequestPermissionsResult: starts");
        switch (requestCode) {
            case REQUEST_CODE_WRITE_STORAGE: {
                //ako je request odbijen zatvori aplikaciju
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Mjetod", "onRequestPermissionsResult: granthil");
                    WRITE_STORAGE_GRANTED = true;
                } else {
                    Log.d("Mjetod", "onRequestPermissionsResult: refjuzn");
                    finish();
                }
            }
        }

        Log.d("Mjetod", "onRequestPermissionsResult: ends");
    }

    //tri metoda pomocu kojih otvaram dijaloge za add, find, edit
    @OnClick(R.id.cv_find)
    public void openDialogFind() {

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


    private void findCase(String string) {
        Log.d("Unesena sifra", string);
        if (string == null || string.trim().equals("")) {
            Toast.makeText(MainActivity.this, "Morate uneti broj", Toast.LENGTH_LONG).show();
        } else {
            QueryBuilder<Slucaj, Integer> qb = null;
            try {
                qb = getDatabaseHelper().getSlucajDao().queryBuilder();
                qb.where().eq(Slucaj.BROJ_SLUCAJA, Integer.parseInt(string));

                List<Slucaj> slucajevi = qb.query();

                Intent intent = new Intent(MainActivity.this, PronadjeniSlucaj.class);
                intent.putExtra(FROM, "find");
                intent.putExtra(CASE_ID, slucajevi.get(0).getId());
                startActivity(intent);
            } catch (IndexOutOfBoundsException e) {
                Toast.makeText(this, "Slucaj sa tom sifrom ne postoji", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Za testiranje dozvoljen unos samo brojeva", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.cv_add)
    public void addNewCase() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_case, null);
        mBuilder.setTitle(R.string.postupak_broj_stranaka);

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

        mBuilder.setPositiveButton("Potvrdi", new DialogInterface.OnClickListener() {
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
        mBuilder.setNegativeButton("Otkazi", new DialogInterface.OnClickListener() {
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
    public void showaLL() {
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
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Slucaj slucaj = (Slucaj) listView.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, PronadjeniSlucaj.class);
                    intent.putExtra(FROM, "all");
                    intent.putExtra(CASE_ID, slucaj.getId());
                    startActivity(intent);
                }
            });


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

    @OnClick(R.id.cv_my_profile)
    public void editMyProfile() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_my_profile, null);
        mBuilder.setTitle("Izaberi postupak");

        EditText ime = mView.findViewById(R.id.dialog_profil_imeprezime);
        EditText adresa = mView.findViewById(R.id.dialog_profil_adresa);
        EditText mesto = mView.findViewById(R.id.dialog_profil_mesto);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ime.setText(sharedPreferences.getString("imeiprezime", ""));
        adresa.setText(sharedPreferences.getString("adresa", ""));
        mesto.setText(sharedPreferences.getString("mesto", ""));

        mBuilder.setPositiveButton("sacuvaj podatke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("imeiprezime", ime.getText().toString());
                editor.putString("adresa", adresa.getText().toString());
                editor.putString("mesto", mesto.getText().toString());
                editor.apply();

                Log.d("tag", "ovo je sacuvano");

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

    public static SharedPreferences getMyProfileSharedPref(String ime, String adresa, String mesto, Context context) {
        SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(context);
        prf.getString(ime, "");
        prf.getString(adresa, "");
        prf.getString(mesto, "");
        Log.d(TAG, "getMyProfileSharedPref: " + " " + prf.getString(ime, "") + " " + prf.getString(adresa, "") + " " + prf.getString(mesto, ""));
        return prf;
    }

    @OnClick(R.id.cv_isprave)
    public void goToIsprave() {

        Intent intent = new Intent(MainActivity.this, IspraveActivity.class);
        startActivity(intent);
    }

    private List<Double> ukupnaCena(List<Slucaj> slc) {
        double trenutnaVrednost = 0;
        ArrayList<Double> listaSvhihTrenutnihCena = new ArrayList<>();
        ListIterator<Slucaj> iterator = null;
        iterator = slc.listIterator();
        Slucaj s;
        while (iterator.hasNext()) {
            s = iterator.next();
            for (IzracunatTrosakRadnje i : s.getListaIzracunatihTroskovaRadnji()) {
                trenutnaVrednost += i.getCena_izracunate_jedinstvene_radnje();
            }
            listaSvhihTrenutnihCena.add(Double.valueOf(trenutnaVrednost));
            trenutnaVrednost = 0;
        }
        return listaSvhihTrenutnihCena;
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

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