package com.example.berka.advokatormlite.activities.add_case.views_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.add_case.OnAddCaseButtonClicked;
import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.PrekrsajContract;
import com.example.berka.advokatormlite.activities.add_case.presenters.PrekrsajPresenter;
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
 * Created by berka on 17-Apr-18.
 */

public class UpperFragmentPrekrsaj extends BaseFragment implements PrekrsajContract.View{

    private static final String TAG = "UpperFragmentPrekrsaj";

    @BindView(R.id.sifraSlucajaPrekrsajni)
    EditText sifraSlucaja;

    @BindView(R.id.tabelaBodovaPrekrsajni)
    Spinner spinner;

    @Inject
    PrekrsajContract.Presenter presenter;

    private Postupak postupak;
    private int broj_stranaka;
    private OnAddCaseButtonClicked onAddCaseButtonClicked;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        App.getMyApp().getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.uper_fragment_prekrsaj, container, false);
        ButterKnife.bind(UpperFragmentPrekrsaj.this, rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //this.presenter.setView(this);
        presenter.setView(this);
        presenter.getVrednostiTabeleBodova(postupak);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        Log.d(TAG, "onDestroy: " + " fragment parnica je otkazan");
    }

    @OnClick(R.id.button_add_prekrsajni)
    @Override
    public void addSlucaj() {

        if(sifraSlucaja.getText().toString().trim().equals("")){
            Toast.makeText(getContext(), "Morate uneti sifru slucaja", Toast.LENGTH_SHORT).show();
        }else {
            onAddCaseButtonClicked.setCase(presenter.saveCaseButtonClicked(
                    Integer.parseInt(sifraSlucaja.getText().toString()),
                    postupak,
                    (TabelaBodova) spinner.getSelectedItem(),
                    Integer.parseInt(null),
                    broj_stranaka));
        }
    }

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    public void emptyCasePasswordWarning() {

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
    public void loadSpinnerTabelaBodova(List<TabelaBodova> tabelaBodova) {

    }
}
