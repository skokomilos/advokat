package com.example.berka.advokatormlite.activities.krivica;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.add_points.PronadjeniSlucajActivity;
import com.example.berka.advokatormlite.activities.add_points.PronadjeniSlucajActivity1;
import com.example.berka.advokatormlite.activities.main.MainActivity;
import com.example.berka.advokatormlite.adapter.StrankaDynamicViews;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.dependencyinjection.App;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by berka on 19-Oct-17.
 */

public class AddKrivicaActivity extends AppCompatActivity implements AddKrivicaContractMVP.View{

    private static final String TAG = "AddKrivicaActivity";
    private static final String POSTUPAK_STATE = "postupak_state";
    private static final String BROJ_STRANAKA_STATE = "broj_stranaka_state";

    @Inject
    AddKrivicaContractMVP.Presenter presenter;

    @BindView(R.id.et_sifra_add_krivica)
    EditText et_sifra_slucaja;

    @BindView(R.id.okrivljen_ostecen_radio_group)
    RadioGroup radioGroup;

    @BindView(R.id.spinnerKrivicaKazne)
    Spinner spinner;

    @BindView(R.id.btn_dodaj_krivicu)
    Button button;

    @BindView(R.id.krivica_gridlayout_za_dinamicko_dodavanje_polja)
    GridLayout gridLayout;


    private int redni_broj_prvog_et_u_grid_ll = 2;

    public static final String FROM = "from";
    public static final String CASE_ID = "case_id";

    private Postupak postupak;
    private int broj_stranaka;

    private StrankaDynamicViews strankaDynamicViews;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_krivica);

        ((App) getApplication()).getComponent().inject(this);

        ButterKnife.bind(AddKrivicaActivity.this);
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

    @Override
    protected void onResume() {
        super.onResume();

        presenter.setView(this);
        presenter.loadSpinners(postupak);
        presenter.addEditTextsProgramacly(broj_stranaka);
    }
    //
    @Override
    public void addRedniBrojStranke(int redni_broj){
        gridLayout.addView(strankaDynamicViews.strankaNumTextView(getApplicationContext(), String.valueOf(redni_broj + 1)));
    }

    @Override
    public void addEditTextZaImeStraneke(){
        gridLayout.addView(strankaDynamicViews.recivedImeEditText(getApplicationContext()));
    }

    @Override
    public void addEditTextZaAdresu(){
        gridLayout.addView(strankaDynamicViews.recivedAdersaEditText(getApplicationContext()));
    }

    @Override
    public void addEditTextZaMesto(){
        gridLayout.addView(strankaDynamicViews.recivedMestoEditText(getApplicationContext()));
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
    public void warrningMessage() {
        Toast.makeText(AddKrivicaActivity.this, "slucaj nije dodat jer je doslo do greske", Toast.LENGTH_LONG).show();
    }

    @Override
    public void loadSpinner(List<TabelaBodova> tabelaBodova){
            ArrayAdapter adapter = new ArrayAdapter(AddKrivicaActivity.this, android.R.layout.simple_spinner_dropdown_item, tabelaBodova);
            spinner.setAdapter(adapter);
    }

    public int okrivljenOstecenValue() {

        String radiovalue =  ((RadioButton)this.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
        return presenter.radioButtonCurrentValue(radiovalue);
    }

    @OnClick(R.id.btn_dodaj_krivicu)
    public void addSlucaj(){
        presenter.saveCaseButtonClicked(et_sifra_slucaja.getText().toString(), postupak, (TabelaBodova)spinner.getSelectedItem(), okrivljenOstecenValue(), broj_stranaka);
    }

    @Override
    public void goToPronadjeniSlucajAcitivty(Slucaj slucaj) {

        Intent intent = new Intent(AddKrivicaActivity.this, PronadjeniSlucajActivity1.class);
        intent.putExtra(FROM, "myDataKey");
        intent.putExtra("myDataKey", slucaj);
        startActivity(intent);
    }

    @Override
    public void emptyCasePasswordWarning() {

        Toast.makeText(AddKrivicaActivity.this, "Morate uneti sifru slucaja", Toast.LENGTH_LONG).show();
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
    public void caseAddedMessage() {
        Toast.makeText(AddKrivicaActivity.this, "Novi je dodat", Toast.LENGTH_LONG).show();
    }
}
