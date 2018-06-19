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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.add_case.OnAddCaseButtonClicked;
import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.ParnicaContract;
import com.example.berka.advokatormlite.activities.add_case.presenters.ParnicaPresenter;
import com.example.berka.advokatormlite.dependencyinjection.App;
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
 * Created by berka on 17-Apr-18.
 */

public class UpperFragmentForParnica  extends BaseFragment implements ParnicaContract.View{

    private static final String TAG = "UpperFragmentForParnica";
    @BindView(R.id.spinner_Vrste_Parnice)
    Spinner spinnerVrstaParnice_ProcenjivoNeprocenjivo;

    @BindView(R.id.spinner_Kazne_tabela_bodova)
    Spinner spinnerTabelaBodova;

    @BindView(R.id.btn_Add_Slucaj_Parnica)
    Button button;

    @BindView(R.id.sifra_Slucaja_Parnica)
    EditText et_sifra_slucaja;

    @Inject
    ParnicaContract.Presenter parnicaPresenter;

    private Postupak postupak;
    private int broj_stranaka;
    private OnAddCaseButtonClicked onAddCaseButtonClicked;

//    public interface OnAddCaseButtonClicked{
//
//        void setCase(Slucaj slucaj);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.upper_fragment_parnica, container, false);
        ButterKnife.bind(UpperFragmentForParnica.this, rootView);

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        App.getMyApp().getComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            broj_stranaka = getArguments().getInt("brojstranaka");
            postupak = getArguments().getParcelable("myDataKey");
        }
    }

    @OnClick(R.id.btn_Add_Slucaj_Parnica)
    @Override
    public void AddSlucaj() {

        if(et_sifra_slucaja.getText().toString().trim().equals("")){
            Toast.makeText(getContext(), "Morate uneti sifru slucaja", Toast.LENGTH_SHORT).show();
        }else {
            onAddCaseButtonClicked.setCase(parnicaPresenter.saveCaseButtonClicked(
                    Integer.parseInt(et_sifra_slucaja.getText().toString()),
                    postupak,
                    (TabelaBodova) spinnerTabelaBodova.getSelectedItem(),
                    broj_stranaka));
        }
    }

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    public void loadSpinnerProcenjivoNeprocenjivo(List<VrsteParnica> vrstaParnica) {

        ArrayAdapter<VrsteParnica> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, vrstaParnica);
        spinnerVrstaParnice_ProcenjivoNeprocenjivo.setAdapter(adapter);
        spinnerVrstaParnice_ProcenjivoNeprocenjivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                parnicaPresenter.getVrednostiTabeleBodovaZaOvajPostupak((VrsteParnica) spinnerVrstaParnice_ProcenjivoNeprocenjivo.getSelectedItem(), postupak);
//                VrsteParnica vrsteParnica = getSelectedVrstaParnicaFromSpinner(spinnerVrsteParnica);
//                presenter.loadTabelaBodova(vrsteParnica, postupak);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void loadSpinnerTabelaBodova(List<TabelaBodova> tabelaBodova){
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, tabelaBodova);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        parnicaPresenter.detachView();
        Log.d(TAG, "onDestroy: " + " fragment parnica je otkazan");
    }

    @Override
    public void onResume() {
        super.onResume();
        parnicaPresenter.setView(this);
        parnicaPresenter.welcomeMessage();
        parnicaPresenter.getVrsteParnica_ProcenjivoNeprocenjivo(postupak);
    }
}
