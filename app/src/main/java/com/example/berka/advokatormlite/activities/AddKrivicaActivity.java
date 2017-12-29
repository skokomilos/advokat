package com.example.berka.advokatormlite.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.adapter.StrankaDynamicViews;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.VrsteParnica;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by berka on 19-Oct-17.
 */

public class AddKrivicaActivity extends AppCompatActivity {

    @BindView(R.id.et_sifra_add_krivica)
    EditText et_sifra_slucaja;

    @BindView(R.id.okrivljen_ostecen_radio_group)
    RadioGroup radioGroup;

    @BindView(R.id.spinnerKrivicaKazne)
    Spinner spinner;

    @BindView(R.id.snp_broj_stranaka)
    ScrollableNumberPicker snpBrojStranaka;

    @BindView(R.id.btn_dodaj_krivicu)
    Button button;

    private GridLayout ll;
    private StrankaDynamicViews strankaDynamicViews;
    private Context context;

    private DatabaseHelper databaseHelper;
    private Postupak postupak;
    private TabelaBodova tabelaBodovaObj;
    private int postupak_id, broj_stranaka;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_krivica);

        initKrivica();
        ButterKnife.bind(AddKrivicaActivity.this);
        loadSpinner();
    }


    private void initKrivica(){

        ll = findViewById(R.id.ll_for_users_info_text_fields);
        postupak_id = getIntent().getExtras().getInt(MainActivity.POSTUPAK_KEY);
        broj_stranaka = getIntent().getExtras().getInt(MainActivity.BROJ_STRANAKA);
        try {
            postupak = getDatabaseHelper().getPostupakDao().queryForId(postupak_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < broj_stranaka; i++) {
           strankaDynamicViews = new StrankaDynamicViews(context);
           ll.addView(strankaDynamicViews.strankaNumTextView(getApplicationContext(), String.valueOf(i + 1)));
           ll.addView(strankaDynamicViews.recivedImeEditText(getApplicationContext()));
           ll.addView(strankaDynamicViews.recivedAdersaEditText(getApplicationContext()));
           ll.addView(strankaDynamicViews.recivedMestoEditText(getApplicationContext()));

        }

//        for (int i = 0; i < broj_stranaka; i++) {
//            EditText editText = new EditText(getApplicationContext());
//            editText.setHint("ime prezime, adresa stanovanje, grad...");
//            Log.d("broj stranaka", String.valueOf(broj_stranaka));
//            ll.addView(editText);
//        }
    }


    private void initKrivica1(){
        postupak_id = getIntent().getExtras().getInt(MainActivity.POSTUPAK_KEY);
        broj_stranaka = getIntent().getExtras().getInt(MainActivity.BROJ_STRANAKA);
        Log.d("testiranje", String.valueOf(postupak_id));
        Log.d("broj stranaka", String.valueOf(broj_stranaka));
        try {
            postupak = getDatabaseHelper().getPostupakDao().queryForId(postupak_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < broj_stranaka; i++) {
            EditText editText = new EditText(getApplicationContext());
            final RelativeLayout ll = findViewById(R.id.probniid1);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            Log.d("vrti", String.valueOf(i));
            ll.addView(editText, params);
        }
    }

    public void loadSpinner(){
        try {
            final List<TabelaBodova> zapreceneKazneList = (List<TabelaBodova>) getDatabaseHelper().getmTabelaBodovaDao().queryBuilder()
                    .where()
                    .eq(TabelaBodova.POSTUPAK_ID, postupak_id)
                    .query();

            ArrayAdapter adapter = new ArrayAdapter(AddKrivicaActivity.this, android.R.layout.simple_spinner_dropdown_item, zapreceneKazneList);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    tabelaBodovaObj = (TabelaBodova) spinner.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getValue(int position) {
        EditText editText = (EditText) ll.getChildAt(position);
        Log.d("prvi", editText.getText().toString());
        return editText.getText().toString();
    }

    @OnClick(R.id.btn_dodaj_krivicu)
    public void addSlucaj(){

//        int broj = 0;
//
//        String radiovalue=  ((RadioButton)this.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
//        if(radiovalue.equals("Okrivljen")){
//            broj = 1;
//        }else if(radiovalue.equals("Ostecen")){
//            broj = 0;
//        }
//
//        Log.d("radio btn", radiovalue + String.valueOf(broj));
//        Log.d("zaprecena kazna", String.valueOf(tabelaBodovaObj.getTarifni_uslov()));
//        Log.d("broj stranaka", String.valueOf(snpBrojStranaka.getValue()));

        for(int i=0; i<broj_stranaka; i++) {
            getValue(i);
        }


//        Slucaj slucaj = new Slucaj();
//        slucaj.setBroj_slucaja(Integer.valueOf(et_sifra_slucaja.getText().toString()));
//        slucaj.setPostupak(postupak);
//        slucaj.setTabelaBodova(tabelaBodovaObj);
//        slucaj.setOkrivljen(radioGroup.getCheckedRadioButtonId());
//        slucaj.setBroj_stranaka(snpBrojStranaka.getValue());
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(AddKrivicaActivity.this, DatabaseHelper.class);
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
