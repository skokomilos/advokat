package com.example.berka.advokatormlite.activities.add_case.views_fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.add_case.AddSlucaj;
import com.example.berka.advokatormlite.activities.add_case.AddSlucajMVP;
import com.example.berka.advokatormlite.activities.add_case.OnAddCaseButtonClicked;
import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.KrivicaContract;
import com.example.berka.advokatormlite.activities.add_case.presenters.KrivicaPresenter;
import com.example.berka.advokatormlite.activities.main.MainActivity;
import com.example.berka.advokatormlite.dependencyinjection.App;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by berka on 17-Apr-18.
 */

public class UpperFragmentForKrivica extends BaseFragment implements KrivicaContract.View, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "UpperFragmentForKrivica";
    private static final String POSTUPAK_STATE = "postupak_state";

    @BindView(R.id.upper_fragment_krivica__et_sifra_slucaja_id)
    EditText et_sifra_slucaja;

    @BindView(R.id.upper_fragment_krivica__radio_group_id)
    RadioGroup radioGroup;

    @BindView(R.id.upper_fragment_krivica_spinnerKrivicaKazne)
    Spinner spinner;

    @BindView(R.id.upper_fragment_krivica_btn_dodaj_krivicu)
    Button button;

    @Inject
    KrivicaContract.Presenter presenter;

    private Postupak postupak;
    private int broj_stranaka;
    private int okrivljen_ostecen;
    private OnAddCaseButtonClicked onAddCaseButtonClicked;


    //method from  implemented fragment/interface
//    public interface OnAddCaseButtonClicked{
//
//        void setCase(Slucaj slucaj);
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        App.getMyApp().getComponent().inject(this);
        //navigationPresenter.addFragment(new UpperFragmentForKrivica());
        //okrivljenOstecenValue();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        ((App) context.getApplicationContext()).getComponent().inject(this);

        try {
            onAddCaseButtonClicked = (OnAddCaseButtonClicked) context;
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            broj_stranaka = getArguments().getInt("brojstranaka");
            postupak = getArguments().getParcelable("myDataKey");
        }
    }

    //    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.uper_fragment_krivica, container, false);
        ButterKnife.bind(UpperFragmentForKrivica.this, rootView);

        radioGroup.setOnCheckedChangeListener(this);
        //navigationPresenter.addFragment(new UpperFragmentForKrivica());

        return rootView;
    }

    @OnClick(R.id.upper_fragment_krivica_btn_dodaj_krivicu)
    public void addSlucaj() {

//        Toast.makeText(getContext(), " Postupak:  " + postupak.getNazivpostupka() + " Okrivljen:  " + String.valueOf(okrivljen_ostecen) + " sifra slucaja " + et_sifra_slucaja.getText().toString() + " broj strana   " + broj_stranaka, Toast.LENGTH_SHORT).show();
//        Slucaj slucaj = presenter.saveCaseButtonClicked(et_sifra_slucaja.getText().toString(), postupak, (TabelaBodova) spinner.getSelectedItem(), radioGroup.getCheckedRadioButtonId(), broj_stranaka);
//        onAddCaseButtonClicked.setCase(slucaj);

//        Slucaj slucaj = new Slucaj();
//        slucaj.setBroj_slucaja(Integer.parseInt(et_sifra_slucaja.getText().toString()));
//        slucaj.setPostupak(postupak);
//        slucaj.setTabelaBodova(null);
//        slucaj.setOkrivljen(radioGroup.getCheckedRadioButtonId());
//        slucaj.setBroj_stranaka(broj_stranaka);
//        Log.d(TAG, "addSlucaj: " + slucaj.getBroj_stranaka() + " " + slucaj.getPostupak().getNazivpostupka() + " " + slucaj.getBroj_slucaja());

        Slucaj slucaj = presenter.saveCaseButtonClicked(Integer.parseInt(et_sifra_slucaja.getText().toString()), postupak, (TabelaBodova) spinner.getSelectedItem(), okrivljen_ostecen, broj_stranaka);
        Log.d(TAG, "hafa: " + ((TabelaBodova) spinner.getSelectedItem()).getBodovi());
        onAddCaseButtonClicked.setCase(slucaj);
        //presenter.saveCaseButtonClicked(et_sifra_slucaja.getText().toString(), postupak, (TabelaBodova) spinner.getSelectedItem(), radioGroup.getCheckedRadioButtonId(), broj_stranaka);
    }

    @Override
    protected int getLayout() {
        return R.layout.uper_fragment_krivica;
    }


    @Override
    public void onResume() {
        super.onResume();
        //this.presenter.setView(this);
        presenter.setView(this);
        presenter.presenterWelcomeMessage();
        presenter.getZapreceneKazne(postupak);
    }



    @Override
    public void emptyCasePasswordWarning() {

        Toast.makeText(getContext(), "Evo radi presenter ", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "emptyCasePasswordWarning: " + " presenter radi u gornjem");
    }

    @Override
    public void numbersOnlyTestingWarning() {

    }

    @Override
    public void caseWithThisCaseAlreadyExists() {

    }

    @Override
    public void caseAddedMessage() {

    }

    @Override
    public void warrningMessage() {

    }

    @Override
    public void loadSpinner(List<TabelaBodova> zapreceneKazne) {

        ArrayAdapter<TabelaBodova> adapter = new ArrayAdapter<TabelaBodova>(getContext(), android.R.layout.simple_spinner_dropdown_item, zapreceneKazne);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.upper_fragment_krivica_rbOkrivljen:
                okrivljen_ostecen = 0;
                Log.d(TAG, "onCheckedChanged: evoradi 0" + okrivljen_ostecen);
                break;
            case R.id.upper_fragment_krivica_rbOstecen:
                okrivljen_ostecen = 1;
                Log.d(TAG, "onCheckedChanged: evoradi 1" + okrivljen_ostecen);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
