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
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.BottomFragmentContract;
import com.example.berka.advokatormlite.activities.add_case.presenters.BottomFragmentPresenter;
import com.example.berka.advokatormlite.adapter.StrankaDynamicViews;
import com.example.berka.advokatormlite.dependencyinjection.App;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by berka on 16-Apr-18.
 */

public class FragmentForDynamicEditText extends Fragment implements BottomFragmentContract.View{

    private static final String TAG = "FragmentForDynamic";

    @Singleton
    @Inject
    BottomFragmentContract.Presenter presenter;

    private int brojstranaka;
    private StrankaDynamicViews strankaDynamicViews;
    private GridLayout gridLayout;
    private Slucaj slucaj1;

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
        Log.d(TAG, "onDestroy: fragment za dinamicko dodoavanje otkazan");
    }

    public void updateFromMain(String s){
        Log.d(TAG, "updateFromMain: " + s);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dynamic, container, false);

        App.getMyApp().getComponent().inject(this);

        if (getArguments() != null) {
             brojstranaka = getArguments().getInt("brojstranaka");
        }

        Toast.makeText(getContext(), "broj stranaka " + String.valueOf(brojstranaka), Toast.LENGTH_SHORT).show();

        gridLayout = rootView.findViewById(R.id.parnica_gridlayout_za_dinamicko_dodavanje_polja);

        for (int i = 0; i < brojstranaka; i++) {
            strankaDynamicViews = new StrankaDynamicViews(getContext());
            gridLayout.addView(strankaDynamicViews.strankaNumTextView(getContext(), String.valueOf(i + 1)));
            gridLayout.addView(strankaDynamicViews.recivedImeEditText(getContext()));
            gridLayout.addView(strankaDynamicViews.recivedAdersaEditText(getContext()));
            gridLayout.addView(strankaDynamicViews.recivedMestoEditText(getContext()));
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.setView(this);
        presenter.tjestMetod("tjestiramo");

    }

    @Override
    public void getCase(Bundle bundle){

        slucaj1 = bundle.getParcelable("slucaj_objekat");

            Log.d(TAG, "broj ili  sifra slucaja je:" + slucaj1.getBroj_slucaja());
            Log.d(TAG, "tabela bodova vrednost slucaja je:" + slucaj1.getTabelaBodova().getBodovi());
            //Log.d(TAG, "orkivljen ili sotecen  slucaja je:" + slucaj1.getOkrivljen());
            Log.d(TAG, "naziv postupka  slucaja je:" + slucaj1.getPostupak().getNazivpostupka());
            Log.d(TAG, "broj stranaka slucaja je:" + slucaj1.getBroj_stranaka());

            presenter.saveCaseDataAndClientsData(slucaj1);
    }

    @Override
    public void goToPronadjeniSlucajActivity() {

        // TODO: 10-May-18 OVO USKORO ODRADITI
        //ime ovoga metod promeniti u nesto jasnije, ovde kreiram intent i odazim na aktivity sa prosirivom listview.
    }

    @Override
    public void addRedniBrojStranke(int redni_broj){
        gridLayout.addView(strankaDynamicViews.strankaNumTextView(getContext(), String.valueOf(redni_broj + 1)));
    }

    @Override
    public void addEditTextZaImeStraneke(){
        gridLayout.addView(strankaDynamicViews.recivedImeEditText(getContext()));
    }

    @Override
    public void addEditTextZaAdresu(){
        gridLayout.addView(strankaDynamicViews.recivedAdersaEditText(getContext()));
    }

    @Override
    public void addEditTextZaMesto(){
        gridLayout.addView(strankaDynamicViews.recivedMestoEditText(getContext()));
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
    public void caseAddedToDataBaseMessage() {

        Toast.makeText(getContext(), "Slucaj uspesno dodat u bazu podataka", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorMessage() {
        
        Toast.makeText(getContext(), "Doslo je do greske", Toast.LENGTH_SHORT).show();
    }
}
