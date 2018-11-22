package com.example.berka.advokatormlite.activities.add_case.views_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.add_case.OnAddCaseButtonClicked;
import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.KrivicaContract;
import com.example.berka.advokatormlite.dependencyinjection.App;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

import javax.inject.Inject;

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
    RadioGroup radioGroupOkrivljenOstecen;

    @BindView(R.id.upper_fragment_krivica__radio_group_vrsta_odbrana_id)
    RadioGroup radioGroupVrsteOdbrana;

    @BindView(R.id.upper_fragment_krivica_spinnerKrivicaKazne)
    Spinner spinner;

    @BindView(R.id.upper_fragment_krivica_btn_dodaj_krivicu)
    Button button;

    @Inject
    KrivicaContract.Presenter presenter;

    private Postupak postupak;
    private int broj_stranaka;
    //vrednosti radio grupa
    private int okrivljen_ostecen;
    private Integer vrsta_odbrane;
    private OnAddCaseButtonClicked onAddCaseButtonClicked;


    //method from  implemented fragment/interface
//    public interface OnAddCaseButtonClicked{
//
//        void setCase(Slucaj slucaj);
//    }

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        App.getMyApp().getComponent().inject(this);
        //navigationPresenter.addFragment(new UpperFragmentForKrivica());
        //okrivljenOstecenValue();
    }

    //    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.uper_fragment_krivica, container, false);
        ButterKnife.bind(UpperFragmentForKrivica.this, rootView);

        radioGroupOkrivljenOstecen.setOnCheckedChangeListener(this);
        radioGroupVrsteOdbrana.setOnCheckedChangeListener(this);
        //navigationPresenter.addFragment(new UpperFragmentForKrivica());

        return rootView;
    }

    @OnClick(R.id.upper_fragment_krivica_btn_dodaj_krivicu)
    public void addSlucaj() {

        if(et_sifra_slucaja.getText().toString().trim().equals("")){
            Toast.makeText(getContext(), "Morate uneti sifru slucaja", Toast.LENGTH_SHORT).show();
        }else {
            if(vrsta_odbrane == null) {
                onAddCaseButtonClicked.setCase(presenter.saveCaseButtonClicked(
                        Integer.parseInt(et_sifra_slucaja.getText().toString()),
                        postupak,
                        (TabelaBodova) spinner.getSelectedItem(),
                        okrivljen_ostecen,
                        broj_stranaka));
            }else {
                onAddCaseButtonClicked.setCase(presenter.saveCaseButtonClicked(
                        Integer.parseInt(et_sifra_slucaja.getText().toString()),
                        postupak,
                        (TabelaBodova) spinner.getSelectedItem(),
                        okrivljen_ostecen,
                        vrsta_odbrane,
                        broj_stranaka));
            }
        }
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
        presenter.getVrednostiTabeleBodova(postupak);
    }

    @Override
    public void emptyCasePasswordWarning() {

        Toast.makeText(getContext(), "Evo radi presenter ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void numbersOnlyTestingWarning() {

    }

    @Override
    public void thisCaseAlreadyExists() {

    }

    @Override
    public void caseAddedMessage() {

    }

    @Override
    public void loadSpinnerTabelaBodova(List<TabelaBodova> zapreceneKazne) {

        ArrayAdapter<TabelaBodova> adapter = new ArrayAdapter<TabelaBodova>(getContext(), android.R.layout.simple_spinner_dropdown_item, zapreceneKazne);
        spinner.setAdapter(adapter);
    }

    /**
     *
     * @param group
     * @param checkedId
     * ako je okrivljen vrednost je 0, ostecen vrednost = 1
     * ako je odbrana po punomocju vrednost 0, ako je sluzbena duznost vrednost 1
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (group.getId()) {
            case R.id.upper_fragment_krivica__radio_group_id:
                switch (checkedId) {
                    case R.id.upper_fragment_krivica_rbOkrivljen:
                        okrivljen_ostecen = 0;
                        for (int i = 0; i < radioGroupVrsteOdbrana.getChildCount(); i++) {
                            radioGroupVrsteOdbrana.getChildAt(i).setEnabled(true);
                            }
                        Log.d(TAG, "onCheckedChanged: okrivljen ili ostecen vrednst: " + okrivljen_ostecen);
                        break;
                    case R.id.upper_fragment_krivica_rbOstecen:
                        okrivljen_ostecen = 1;
                        vrsta_odbrane = null;
                        for (int i = 0; i < radioGroupVrsteOdbrana.getChildCount(); i++) {
                            radioGroupVrsteOdbrana.getChildAt(i).setEnabled(false);
                        }
                        Log.d(TAG, "onCheckedChanged: okrivljen ili ostecen vrednost: " + okrivljen_ostecen);
                        break;
                }
            case R.id.upper_fragment_krivica__radio_group_vrsta_odbrana_id:
                switch (checkedId) {
                    case R.id.upper_fragment_krivica_odbrana_po_punomocju_id:
                        vrsta_odbrane = 0;
                        Log.d(TAG, "onCheckedChanged: vrsta odbrane: " + vrsta_odbrane);
                        break;
                    case R.id.upper_fragment_krivica_odbrana_sluzbena_duznost_id:
                        vrsta_odbrane = 1;
                        Log.d(TAG, "onCheckedChanged: vrsta odbrane: " + vrsta_odbrane);
                        break;
                }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
