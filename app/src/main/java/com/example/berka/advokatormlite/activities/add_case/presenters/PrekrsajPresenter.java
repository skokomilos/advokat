package com.example.berka.advokatormlite.activities.add_case.presenters;

import android.util.Log;

import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.PrekrsajContract;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 18-Apr-18.
 */

public class PrekrsajPresenter implements PrekrsajContract.Presenter{

    private static final String TAG = "PrekrsajPresenter";
    PrekrsajContract.View view;
    PrekrsajContract.Model model;

    public PrekrsajPresenter(PrekrsajContract.Model model) {
        this.model = model;
    }

    @Override
    public void setView(PrekrsajContract.View view) {
            this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void getVrednostiTabeleBodova(Postupak postupak) {

        List<TabelaBodova> zapreceneKazne;
        if(!postupak.equals(null)){
            zapreceneKazne = model.getZapreceneKazne(postupak);
            view.loadSpinnerTabelaBodova(zapreceneKazne);
        }
    }

    @Override
    public Slucaj saveCaseButtonClicked(int broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int vrsta_odbrane, int broj_stranaka) {
        return null;
    }

    @Override
    public Slucaj saveCaseButtonClicked(int broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka) {
        return null;
    }

    @Override
    public Slucaj saveCaseButtonClicked(int broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int broj_stranaka) {

        Slucaj slucaj = new Slucaj();
        slucaj.setBroj_slucaja(broj_slucaja);
        slucaj.setPostupak(postupak);
        slucaj.setTabelaBodova(selectedItem);
        slucaj.setBroj_stranaka(broj_stranaka);
        Log.d(TAG, "saveCaseButtonClicked: " + slucaj.getBroj_slucaja() + " slucaj je u presenteru");
        return slucaj;
    }

}
