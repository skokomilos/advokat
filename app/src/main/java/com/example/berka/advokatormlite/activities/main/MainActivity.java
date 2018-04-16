package com.example.berka.advokatormlite.activities.main;

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
import com.example.berka.advokatormlite.activities.add_points.PronadjeniSlucajActivity;
import com.example.berka.advokatormlite.activities.add_points.PronadjeniSlucajActivity1;
import com.example.berka.advokatormlite.activities.krivica.AddKrivicaActivity;
import com.example.berka.advokatormlite.activities.parnica.AddParnicaActivity;
import com.example.berka.advokatormlite.activities.prekrsaj.AddPrekrsajniActivity;
import com.example.berka.advokatormlite.activities.ustavni.AddUstavniActivity;
import com.example.berka.advokatormlite.activities.BaseActivity;
import com.example.berka.advokatormlite.activities.IspraveActivity;
import com.example.berka.advokatormlite.adapter.MyAdapterSviSlucajevi;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.dependencyinjection.App;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends BaseActivity implements MainActivityContractMVP.View{


    //sources:
    //https://github.com/andreasschrade/android-design-template
    //https://github.com/afollestad/material-dialogs

    public static final String FROM = "from";
    public static final String CASE_ID = "case_id";

    public static String POSTUPAK_KEY = "POSTUPAK_KEY";
    public static String BROJ_STRANAKA = "BROJ_STRANAKA";

    @Inject
    MainActivityContractMVP.Presenter presenter;

    //permission
    public static final int REQUEST_CODE_WRITE_STORAGE = 123;
    public static boolean WRITE_STORAGE_GRANTED = false;
    public static final String TAG = "MainActivity";


    public static final String FROM_MAIN = "from_main";
    public static final String CASE_TYPE = "case_type";

    private MyAdapterSviSlucajevi slucajeviAdapter;

    private Unbinder unbinder;

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

    private EditText lawyerName, lawyerAddress, lawyerPlace;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_main);

        ((App) getApplication()).getComponent().inject(this);

        isWriteStorageGranted();
        setupToolbar();
        ButterKnife.bind(this,MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
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

        switch (requestCode) {
            case REQUEST_CODE_WRITE_STORAGE: {
                //ako je request odbijen zatvori aplikaciju
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    WRITE_STORAGE_GRANTED = true;
                } else {
                    finish();
                }
            }
        }
    }

    @OnClick(R.id.cv_my_profile)
    public void editMyProfile() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_my_profile, null);
        mBuilder.setTitle("Izaberi postupak");

        lawyerName = mView.findViewById(R.id.dialog_profil_imeprezime);
        lawyerAddress = mView.findViewById(R.id.dialog_profil_adresa);
        lawyerPlace = mView.findViewById(R.id.dialog_profil_mesto);

        //ovde popunjavam ime, adresu, mesto ako je sacuvano to zamenjuje ovo dadfole
        presenter.prefUserDialogCreated();

        mBuilder.setPositiveButton("sacuvaj podatke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                presenter.prefUserDialogSaveData(lawyerName.getText().toString(), lawyerAddress.getText().toString(), lawyerPlace.getText().toString());
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
    public void updateLawyerName(String userName) {
        lawyerName.setText(userName);
    }

    @Override
    public void updateLawyerAddress(String address) {
        lawyerAddress.setText(address);
    }

    @Override
    public void updateLawyerPlace(String place) {
        lawyerPlace.setText(place);
    }

    @OnClick(R.id.cv_isprave)
    public void goToIsprave() {

        Intent intent = new Intent(MainActivity.this, IspraveActivity.class);
        startActivity(intent);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void setListSlucajevi(List<Slucaj> slucajevi) {

    }

    @OnClick(R.id.cv_add)
    @Override
    public void clickAddCard() {
        presenter.getAllPostupci();
    }


    @OnClick(R.id.cv_all)
    @Override
    public void clickAllCard() {
        presenter.getAllCases();
    }

    @Override
    public void caseNotFound() {
        Toast.makeText(this, "ne postoji slucaj sa tom sifrom", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.cv_find)
    @Override
    public void clickFindCard() {
        new MaterialDialog.Builder(this)
                .title("Moj Dialog")
                .content("Unesi sifru slucaja")
                .inputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                        | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(1, 16)
                .positiveText("Submit")
                .input("", "", false, (dialog, input) -> presenter.findCaseButtonClicked(input.toString())).show();
    }

    @Override
    public void gotoFoundCaseFromFindCaseDialog(Slucaj slucaj) {
        Log.d(TAG, "gotoFoundCaseFromFindCaseDialog: " + slucaj.getBroj_stranaka());
        Intent intent = new Intent(MainActivity.this, PronadjeniSlucajActivity1.class);
        intent.putExtra(FROM, "myDataKey");
        intent.putExtra("myDataKey", slucaj);
        startActivity(intent);
    }

    @Override
    public void gotoFoundCaseFromAllCasesDialog(Slucaj slucaj) {
        Intent intent = new Intent(MainActivity.this, PronadjeniSlucajActivity1.class);
        intent.putExtra(FROM, "all");
        intent.putExtra(CASE_ID, slucaj.getId());
        startActivity(intent);
    }

    @Override
    public String getCaseId() {
        EditText et_case_id = findViewById(R.id.dialog_et_broj_slucaja);
        return et_case_id.getText().toString();
    }

    @Override
    public void showNoValuesTypedError() {
        Toast.makeText(this, "Morate uneti broj slucaja", Toast.LENGTH_LONG).show();
    }

    @Override
    public void enterNumberOfClients() {
        Toast.makeText(this, "Morate uneti broj stranaka", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAddCaseDialog(List<Postupak> postupci) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Izaberi postupak");
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_case, null);

        EditText number_ofclients = mView.findViewById(R.id.broj_stranaka);
        Spinner spinner_sapostupcima = mView.findViewById(R.id.spnDialogAdd);

        number_ofclients.setText("1");

        ArrayAdapter<Postupak> adapter = new ArrayAdapter<Postupak>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, postupci);
        spinner_sapostupcima.setAdapter(adapter);


        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.addCaseButtonClicked((Postupak)spinner_sapostupcima.getSelectedItem(), number_ofclients.getText().toString());
                Log.d(TAG, "broj stranaka: " + number_ofclients.getText().toString());
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

    @Override
    public void showAllCasesDialog(List<Slucaj> slucajevi, List<Double> izracunateTarife) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Izaberi postupak");

        View mView = getLayoutInflater().inflate(R.layout.dialog_all_cases, null);
        ListView listView = mView.findViewById(R.id.lista_svih_slucajeva);

        MyAdapterSviSlucajevi slucajeviAdapter = new MyAdapterSviSlucajevi(this, slucajevi, izracunateTarife);
        listView.setAdapter(slucajeviAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //presenter.caseItemClicked((Slucaj)listView.getItemAtPosition(position));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Slucaj slucaj = (Slucaj) listView.getItemAtPosition(position);
                        gotoFoundCaseFromAllCasesDialog(slucaj);

                    }
                });

                mBuilder.setNegativeButton("Izadji", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    @Override
    public void displayNoPostupci() {
        Toast.makeText(this, "E druze ovde nema nikakvih postupaka", Toast.LENGTH_LONG).show();
    }

    @Override
    public void startIntentKrivica(Postupak postupak, int broj_stranaka) {
//        Intent intent = new Intent(MainActivity.this, AddKrivicaActivity.class);
//        Log.d(TAG, "postupak koji saljem: " + postupak);
//        intent.putExtra(POSTUPAK_KEY, postupak);
//        intent.putExtra(BROJ_STRANAKA, broj_stranaka);
//        startActivity(intent);

        //Log.d(TAG, "gotoFoundCaseFromFindCaseDialog: " + slucaj.getBroj_stranaka());
        Intent intent = new Intent(MainActivity.this, AddKrivicaActivity.class);
        intent.putExtra(FROM, "myDataKey");
        intent.putExtra("myDataKey", postupak);
        intent.putExtra("numberOfParties", broj_stranaka);
        startActivity(intent);
    }

    @Override
    public void startIntentPrekrsaj(int postupakId, int broj_stranaka) {
        Intent intent = new Intent(MainActivity.this, AddPrekrsajniActivity.class);
        intent.putExtra(POSTUPAK_KEY, postupakId);
        intent.putExtra(BROJ_STRANAKA, broj_stranaka);
        startActivity(intent);
    }

    @Override
    public void startIntentIsprave(int postupakId, int broj_stranaka) {
        Intent intent = new Intent(MainActivity.this, AddUstavniActivity.class);
        intent.putExtra(POSTUPAK_KEY, postupakId);
        intent.putExtra(BROJ_STRANAKA, broj_stranaka);
        startActivity(intent);
    }

    @Override
    public void startIntentOstaleKojeImajuProcenjivoNeprocenjivo(int postupakId, int broj_stranaka) {
        Intent intent = new Intent(MainActivity.this, AddParnicaActivity.class);
        intent.putExtra(POSTUPAK_KEY, postupakId);
        intent.putExtra(BROJ_STRANAKA, broj_stranaka);
        startActivity(intent);
    }

    @Override
    public void showCaseSavedMessage() {
        Toast.makeText(this, "slucaj je sacuvan u bazu", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showOnlyNumbers() {
        Toast.makeText(this, "samo brojevi zbog testiranja", Toast.LENGTH_LONG).show();
    }

    @Override
    public void pleaseEnterNumber() {
        Toast.makeText(this, "morate uneti broj", Toast.LENGTH_LONG).show();
    }

    @Override
    public void emptyEditTextWarning() {

    }

    @Override
    public void gotoSpecificCaseActivity(Class activity, int postupakID, String brojStranaka) {
        Intent intent = new Intent(MainActivity.this, activity);
        intent.putExtra(POSTUPAK_KEY, postupakID);
        intent.putExtra(BROJ_STRANAKA, Integer.parseInt(String.valueOf(brojStranaka)));
        startActivity(intent);
    }
}