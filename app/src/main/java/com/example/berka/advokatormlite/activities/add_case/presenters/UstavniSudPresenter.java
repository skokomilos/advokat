package com.example.berka.advokatormlite.activities.add_case.presenters;

import android.util.Log;

import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.UstavniSudContract;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;

/**
 * Created by berka on 18-Apr-18.
 */

public class UstavniSudPresenter implements UstavniSudContract.Presenter{


    private static final String TAG = "UstavniSudPresenter";
    UstavniSudContract.View view;
    UstavniSudContract.Model model;

    public UstavniSudPresenter(UstavniSudContract.Model model) {
        this.model = model;
    }

    @Override
    public void setView(UstavniSudContract.View view) {

        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public Slucaj saveCaseButtonClicked(int broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int vrsta_odbrane, int broj_stranaka) {

        Slucaj slucaj = new Slucaj();
        slucaj.setBroj_slucaja(broj_slucaja);
        slucaj.setPostupak(postupak);
        slucaj.setTabelaBodova(selectedItem);
        slucaj.setBroj_stranaka(broj_stranaka);
        Log.d(TAG, "saveCaseButtonClicked: " + slucaj.getBroj_slucaja() + " slucaj je u presenteru");
        return slucaj;
    }

    @Override
    public Slucaj saveCaseButtonClicked(int broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka) {
        return null;
    }

    @Override
    public void getVrednostiTabeleBodova(Postupak postupak) {

        TabelaBodova fiksniBodoviUstavniSud;
        if(!postupak.equals(null)){
            fiksniBodoviUstavniSud = (TabelaBodova) model.getZapreceneKazne(postupak);
            Log.d(TAG, "getZapreceneKazne: " + fiksniBodoviUstavniSud.getBodovi());
            view.loadTabelaBodovaVrednost(fiksniBodoviUstavniSud);
        }
    }

    @Override
    public Slucaj saveCaseButtonClicked(int broj_slucaja, Postupak postupak, TabelaBodova tabelaBodova, int broj_stranaka) {

        Slucaj slucaj = new Slucaj();
        slucaj.setBroj_slucaja(broj_slucaja);
        slucaj.setPostupak(postupak);
        slucaj.setTabelaBodova(tabelaBodova);
        slucaj.setBroj_stranaka(broj_stranaka);
        Log.d(TAG, "saveCaseButtonClicked: " + slucaj.getBroj_slucaja() + " slucaj je u presenteru");
        return slucaj;
    }
}
