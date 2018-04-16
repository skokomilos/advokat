package com.example.berka.advokatormlite.activities.ustavni;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.add_points.PronadjeniSlucajActivity;
import com.example.berka.advokatormlite.activities.main.MainActivity;
import com.example.berka.advokatormlite.adapter.StrankaDynamicViews;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by berka on 06-Nov-17.
 */

public class AddUstavniActivity extends AppCompatActivity implements AddUstavniContract.View {

    private static final String FROM = "from";
    private static final String CASE_ID = "case_id";
    private DatabaseHelper databaseHelper;

    @BindView(R.id.sifraSlucajaUstavni)
    EditText et_sifra;

    @BindView(R.id.button_add_ustavni)
    Button addSlucaj;
    
    @BindView(R.id.ustavni_gridlayout_za_dinamicko_dodavanje_polja)
    GridLayout gridLayout;

    Context context;

    AddUstavniPresenter presenter;

    private int redni_broj_prvog_et_u_grid_ll = 2;

    public final int TABELA_BODOVA_ID = 47;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ustavni);

        ButterKnife.bind(AddUstavniActivity.this);
        presenter = new AddUstavniPresenter(this, getDatabaseHelper());
        getBrojStranakaFromMainActivityIntent();
        addEditTextsProgramabilno();
        int postupakId = getPostupakIdFromMainActivityIntent();
        presenter.loadZapreceneKazne(postupakId);
    }

    @Override
    public int getPostupakIdFromMainActivityIntent() {
        int postupakId;
        postupakId = getIntent().getExtras().getInt(MainActivity.POSTUPAK_KEY);
        return postupakId;
    }

    @Override
    public int getBrojStranakaFromMainActivityIntent() {
        int broj_stranaka;
        broj_stranaka = getIntent().getExtras().getInt(MainActivity.BROJ_STRANAKA);
        return broj_stranaka;
    }

    @Override
    public void addEditTextsProgramabilno() {
        for (int i = 0; i < getBrojStranakaFromMainActivityIntent(); i++) {
            StrankaDynamicViews strankaDynamicViews = new StrankaDynamicViews(context);
            gridLayout.addView(strankaDynamicViews.strankaNumTextView(getApplicationContext(), String.valueOf(i + 1)));
            gridLayout.addView(strankaDynamicViews.recivedImeEditText(getApplicationContext()));
            gridLayout.addView(strankaDynamicViews.recivedAdersaEditText(getApplicationContext()));
            gridLayout.addView(strankaDynamicViews.recivedMestoEditText(getApplicationContext()));
        }
    }

    @Override
    public StrankaDetail getDetailValuesForSingleClient(int position) {
        StrankaDetail strankaDetail = new StrankaDetail();
        EditText imeprezime = (EditText) gridLayout.getChildAt(position);
        EditText adresa = (EditText) gridLayout.getChildAt(position+1);
        EditText mesto = (EditText) gridLayout.getChildAt(position+2);

        strankaDetail.setIme_i_prezime(imeprezime.getText().toString());
        strankaDetail.setAdresa(adresa.getText().toString());
        strankaDetail.setMesto(mesto.getText().toString());

        return strankaDetail;
    }

    @Override
    public ArrayList<StrankaDetail> allClientsDetailValues() {
        ArrayList<StrankaDetail> myArrayListSvihStranakaSaPodacima = new ArrayList<>();
        for(int i=0; i<getBrojStranakaFromMainActivityIntent(); i++) {

            //getValue(input);
            myArrayListSvihStranakaSaPodacima.add(getDetailValuesForSingleClient(redni_broj_prvog_et_u_grid_ll));
            redni_broj_prvog_et_u_grid_ll+=4;
        }
        return myArrayListSvihStranakaSaPodacima;
    }

    @Override
    public void loadSpinner(List<TabelaBodova> sveZapreceneKazne) {
        //todo ne treba
    }

    @Override
    public void gotoFoundCaseActivity(int caseId) {
        Intent intent = new Intent(AddUstavniActivity.this, PronadjeniSlucajActivity.class);
        intent.putExtra(FROM, "find");
        intent.putExtra(CASE_ID, caseId);
        startActivity(intent);
    }

    @Override
    public String getCasePasswordFromEditText() {
        return et_sifra.getText().toString();
    }

    @Override
    public TabelaBodova getSelectedZaprecenaKazna(Spinner spinner) {
        return null;
    }

    @Override
    public void emptyCasePasswordWarning() {
        Toast.makeText(this, "Morate uneti sifru slucaja", Toast.LENGTH_LONG).show();
    }

    @Override
    public void numbersOnlyTestingWarning() {
        Toast.makeText(this, "Za testiranje dozvoljen unos samo brojeva", Toast.LENGTH_LONG).show();
    }

    @Override
    public void caseWithThisCaseAlreadyExists() {
        Toast.makeText(this, "Verovatno vec postoji slucaj sa ovom sifrom", Toast.LENGTH_LONG).show();
    }

    @Override
    public void caseAddedMessage(Slucaj slucaj) {
        Toast.makeText(AddUstavniActivity.this, "Novi je dodat\n" +
                "broj slucaja: "+slucaj.getBroj_slucaja() + "\n" +
                "tabela bodova: "+slucaj.getTabelaBodova() + "\n",
                Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.button_add_ustavni)
    public void addSlucaj(){
        presenter.saveCasetoDB(getCasePasswordFromEditText(), presenter.loadPostupak(getPostupakIdFromMainActivityIntent()), presenter.getSpecificObjectFromTabelaBodova(TABELA_BODOVA_ID), getBrojStranakaFromMainActivityIntent());
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
