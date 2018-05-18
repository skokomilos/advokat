package com.example.berka.advokatormlite.activities.add_case.presenters;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.Toast;

import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.KrivicaContract;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentForKrivica;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentForParnica;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentPrekrsaj;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentUstavniSud;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 18-Apr-18.
 */

public class KrivicaPresenter implements KrivicaContract.Presenter{

    private static final String TAG = "KrivicaPresenter";
    @Nullable
    private KrivicaContract.View view;
    private KrivicaContract.Model model;

    public KrivicaPresenter(KrivicaContract.Model model) {
        this.model = model;
    }

    @Override
    public void setView(KrivicaContract.View view) {
        this.view = view;
    }

    @Override
    public void getZapreceneKazne(Postupak postupak) {

        List<TabelaBodova> zapreceneKazne;
        if(!postupak.equals(null)){
            zapreceneKazne = model.getZapreceneKazne(postupak);
            Log.d(TAG, "getZapreceneKazne: " + zapreceneKazne.get(0).getTarifni_uslov());
            view.loadSpinner(zapreceneKazne);
        }

    }

    @Override
    public int radioButtonCurrentValue(String radiovalue) {
        return 0;
    }

    @Override
    public Slucaj saveCaseButtonClicked(int broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka) {

                Slucaj slucaj = new Slucaj();
                slucaj.setBroj_slucaja(broj_slucaja);
                slucaj.setPostupak(postupak);
                slucaj.setTabelaBodova(selectedItem);
                slucaj.setOkrivljen(valueOkrivljenOstecen);
                slucaj.setBroj_stranaka(broj_stranaka);
                Log.d(TAG, "saveCaseButtonClicked: " + slucaj.getBroj_slucaja() + " slucaj je u presenteru");
                return slucaj;
    }

    @Override
    public void detachView() {

        view = null;
    }

    @Override
    public void presenterWelcomeMessage() {

        view.emptyCasePasswordWarning();
    }

}
