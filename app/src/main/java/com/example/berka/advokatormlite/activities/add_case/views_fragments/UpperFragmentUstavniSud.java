package com.example.berka.advokatormlite.activities.add_case.views_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.add_case.OnAddCaseButtonClicked;
import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.UstavniSudContract;
import com.example.berka.advokatormlite.dependencyinjection.App;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.TabelaBodova;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by berka on 17-Apr-18.
 */

public class UpperFragmentUstavniSud  extends BaseFragment implements UstavniSudContract.View{

    private TabelaBodova tabelaBodova;

    private static final String TAG = "UpperFragmentUstavniSud";

    //interface for joining bottom and upper fragment
    private OnAddCaseButtonClicked onAddCaseButtonClicked;
    private Postupak postupak;
    private int broj_stranaka;

    @BindView(R.id.sifraSlucajaUstavni)
    EditText et_sifraSlucaja;

    @BindView(R.id.button_add_ustavni)
    Button button;

    @Inject
    UstavniSudContract.Presenter presenter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        App.getMyApp().getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.upper_fragment_ustavni_sud, container, false);
        ButterKnife.bind(UpperFragmentUstavniSud.this, rootView);

        return rootView;
    }

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

        if(getArguments()!=null){
            broj_stranaka = getArguments().getInt("brojstranaka");
            postupak = getArguments().getParcelable("myDataKey");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.setView(this);
        presenter.getVrednostiTabeleBodova(postupak);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @OnClick(R.id.button_add_ustavni)
    @Override
    public void addSlucaj(){

        Log.d(TAG, "addSlucaj: pritsuto");
        if(et_sifraSlucaja.getText().toString().trim().equals("")){
            Toast.makeText(getContext(), "Morate uneti sifru slucaja", Toast.LENGTH_SHORT).show();
        }else {
            onAddCaseButtonClicked.setCase(presenter.saveCaseButtonClicked(
                    Integer.parseInt(et_sifraSlucaja.getText().toString()),
                    postupak,
                    tabelaBodova,
                    broj_stranaka));
        }
    }

    @Override
    protected int getLayout() {
        return 0;
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
    public void loadTabelaBodovaVrednost(TabelaBodova fiksniBodoviUstavniSud) {

        tabelaBodova = fiksniBodoviUstavniSud;
        Log.d(TAG, "loadTabelaBodovaVrednost: " + tabelaBodova.getBodovi());
    }


}
