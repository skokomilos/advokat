package com.example.berka.advokatormlite.activities.parnica;

import android.content.Context;
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

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.adapter.StrankaDynamicViews;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.VrsteParnica;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by berka on 19-Oct-17.
 */

public class AddParnicaActivity extends AppCompatActivity implements AddParnicaContractMVP.View {

    private static final String TAG = "AddParnicaActivity";
    private static final String POSTUPAK_STATE = "postupak_state";
    private DatabaseHelper databaseHelper;

    private StrankaDynamicViews strankaDynamicViews;
    private Context context;

    public static final String FROM = "from";
    public static final String CASE_ID = "case_id";

    @Inject
    AddParnicaContractMVP.Presenter presenter;

    @BindView(R.id.btnAddSlucajParnica)
    Button btnAddSlucajParnica;

    int redni_broj_prvog_et_u_grid_ll = 2;

    @BindView(R.id.parnica_gridlayout_za_dinamicko_dodavanje_polja)
    GridLayout gridLayout;

    @BindView(R.id.sifraSlucajaParnica)
    EditText  et_sifra_slucaja;;

    @BindView(R.id.spinnerVrsteParnice)
    Spinner spinnerVrsteParnica;

    @BindView(R.id.spinnerKazne)
    Spinner spinnerTabelaBodova;

    Postupak postupak;
    private int broj_stranaka;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parnica);

        ButterKnife.bind(AddParnicaActivity.this);
        strankaDynamicViews = new StrankaDynamicViews(this);

        if (savedInstanceState != null) {
            postupak = savedInstanceState.getParcelable(POSTUPAK_STATE);
            //broj_stranaka = savedInstanceState.getParcelable(BROJ_STRANAKA_STATE);
        } else {
            postupak = getIntent().getParcelableExtra("myDataKey");
            Log.d(TAG, "naziv postu: " + postupak.getNazivpostupka());
            broj_stranaka = getIntent().getExtras().getInt("numberOfParties");
        }
    }

    /////////////////////////////////////////////
    ///////////////////////////////////////////


    @Override
    public void addredniBrojStranke(int redni_broj) {
        gridLayout.addView(strankaDynamicViews.strankaNumTextView(getApplicationContext(), String.valueOf(redni_broj + 1)));
    }

    @Override
    public void addEditTextZaImeStraneke() {
        gridLayout.addView(strankaDynamicViews.recivedImeEditText(getApplicationContext()));
    }

    @Override
    public void addEditTextZaAdresu() {
        gridLayout.addView(strankaDynamicViews.recivedAdersaEditText(getApplicationContext()));
    }

    @Override
    public void addEditTextZaMesto() {
        gridLayout.addView(strankaDynamicViews.recivedMestoEditText(getApplicationContext()));
    }

    @Override
    public void noCasePasswordWarning() {

    }

    @Override
    public String getClientName(int redni_broj_edit_texta_u_grid_layout) {

        EditText imeiprezime = (EditText) gridLayout.getChildAt(redni_broj_edit_texta_u_grid_layout);
        return imeiprezime.getText().toString();
    }

    @Override
    public String getClientAddress(int i) {

        EditText adresa = (EditText) gridLayout.getChildAt(i);
        return adresa.getText().toString();
    }

    @Override
    public String getClientPlace(int i) {

        EditText mesto = (EditText) gridLayout.getChildAt(i);
        return mesto.getText().toString();
    }

    @Override
    public void caseExistsWarning() {

    }

    @Override
    public void caseAddesMessage() {

    }

    @Override
    public void setContentView(View view) {

    }

    @Override
    public void goToPronadjeniSlucajAcitivty(Slucaj slucaj) {

    }

    @Override
    public void emptyCasePasswordWarning() {

    }

    @OnClick(R.id.btnAddSlucajParnica)
    public void addSlucaj() {
        //presenter.saveCaseButtonClicked(et_sifra_slucaja.getText().toString(), postupak, (TabelaBodova)spinner.getSelectedItem(), okrivljenOstecenValue(), broj_stranaka);

    }

    @Override
    public void loadSpinnerPromenljivoNepromenljivo(List<VrsteParnica> vrstaParnica) {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, vrstaParnica);
        spinnerVrsteParnica.setAdapter(adapter);
        spinnerVrsteParnica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                VrsteParnica vrsteParnica = getSelectedVrstaParnicaFromSpinner(spinnerVrsteParnica);
//                presenter.loadTabelaBodova(vrsteParnica, postupak);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void loadSpinner(List<TabelaBodova> tabelaBodova){
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tabelaBodova);
        spinnerTabelaBodova.setAdapter(adapter);
        spinnerTabelaBodova.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                getSelectedZaprecenaKazna(spinnerTabelaBodova);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
