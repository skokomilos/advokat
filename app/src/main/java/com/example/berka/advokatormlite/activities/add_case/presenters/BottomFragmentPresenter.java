package com.example.berka.advokatormlite.activities.add_case.presenters;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.BottomFragmentContract;
import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.KrivicaContract;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.FragmentForDynamicEditText;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.BottomFragmentContract;
import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.KrivicaContract;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.FragmentForDynamicEditText;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;

/**
 * Created by berka on 23-Apr-18.
 */

public class BottomFragmentPresenter implements BottomFragmentContract.Presenter{

    private static final String TAG = "BottomFragmentPresenter";
    @Nullable
    private BottomFragmentContract.View view;
    private BottomFragmentContract.Model model;

    public BottomFragmentPresenter(BottomFragmentContract.Model model) {
        this.model = model;
    }

    @Override
    public void addEditTextsForClient(int broj_stranaka) {
                for (int i = 0; i < broj_stranaka; i++) {
                    view.addRedniBrojStranke(i);
                    view.addEditTextZaImeStraneke();
                    view.addEditTextZaAdresu();
                    view.addEditTextZaMesto();
        }
    }

    @Override
    public void saveCaseDataAndClientsData(Slucaj slucaj) {

        Log.d(TAG, "saveCaseDataAndClientsData: ovo je krajnja destinacija ovde kreiram slucaj i dodajem klijente" + slucaj.getBroj_slucaja());

        //todo ovde verovatno treba asinhrono ubacivanje ubazu a zatum ubaciti durgi deo
        model.saveSlucaj(slucaj);

        if(slucaj != null){

            int redni_broj_edit_texta_u_grid_layout = 2;

            for (int i = 0; i < slucaj.getBroj_stranaka(); i++) {

                StrankaDetail strankaDetail = new StrankaDetail();
                strankaDetail.setIme_i_prezime(view.getClientName(redni_broj_edit_texta_u_grid_layout));
                strankaDetail.setAdresa(view.getClientAddress(redni_broj_edit_texta_u_grid_layout + 1));
                strankaDetail.setMesto(view.getClientPlace(redni_broj_edit_texta_u_grid_layout + 2));
                strankaDetail.setSlucaj(slucaj);

                model.saveStrankaDetails(strankaDetail);
                redni_broj_edit_texta_u_grid_layout += 4;
            }
        }else {
            view.errorMessage();
        }

        view.caseAddedToDataBaseMessage();
        view.goToPronadjeniSlucajActivity();

    }

    @Override
    public void tjestMetod(String cao) {

        Log.d(TAG, "tjestMetod: " + cao);
    }

    @Override
    public void setView(BottomFragmentContract.View view) {

        this.view = view;
    }

    @Override
    public void detachView() {

        this.view = null;
    }
}
