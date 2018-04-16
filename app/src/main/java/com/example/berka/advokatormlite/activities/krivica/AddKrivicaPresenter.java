package com.example.berka.advokatormlite.activities.krivica;

import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.example.berka.advokatormlite.adapter.StrankaDynamicViews;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.j256.ormlite.field.types.NativeUuidType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by berka on 22-Feb-18.
 */

public class AddKrivicaPresenter implements AddKrivicaContractMVP.Presenter {

    private static final String TAG = "AddKrivicaPresenter";
    @Nullable
    private AddKrivicaContractMVP.View view;
    private AddKrivicaContractMVP.Model model;

    public AddKrivicaPresenter(AddKrivicaContractMVP.Model model) {
        this.model = model;
    }

    @Override
    public void addEditTextsProgramacly(int broj_stranaka) {
                for (int i = 0; i < broj_stranaka; i++) {
                    view.addRedniBrojStranke(i);
                    view.addEditTextZaImeStraneke();
                    view.addEditTextZaAdresu();
                    view.addEditTextZaMesto();
        }
    }

    @Override
    public void setView(AddKrivicaContractMVP.View view) {
            this.view = view;
    }

    @Override
    public void loadSpinners(Postupak postupak) {

        if(view != null) {
            List<TabelaBodova> zaprecenjekazne = model.zaprecenjeKazneZaKrivicu(postupak);
            if(zaprecenjekazne != null){
                view.loadSpinner(zaprecenjekazne);
            }
        }
    }

    @Override
    public int radioButtonCurrentValue(String radiovalue) {

        int currentValue = 0;

        if(radiovalue.equals("Okrivljen")){
            currentValue = 1;
        }else if(radiovalue.equals("Ostecen")){
            currentValue = 0;
        }
        return currentValue;
    }

    @Override
    public void saveCaseButtonClicked(String broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka) {

        //ovde pozvati repository
        Slucaj slucaj = model.saveSlucaj(broj_slucaja, postupak, selectedItem, valueOkrivljenOstecen, broj_stranaka);
        Log.d(TAG, "saveCaseButtonClicked: " + broj_slucaja + " " + postupak.getNazivpostupka() + " " + selectedItem.getBodovi() + " " + valueOkrivljenOstecen + " " + broj_stranaka);

        if(slucaj != null){

            int redni_broj_edit_texta_u_grid_layout = 2;

            for (int i = 0; i < broj_stranaka; i++) {

                StrankaDetail strankaDetail = new StrankaDetail();
                strankaDetail.setIme_i_prezime(view.getClientName(redni_broj_edit_texta_u_grid_layout));
                strankaDetail.setAdresa(view.getClientAddress(redni_broj_edit_texta_u_grid_layout + 1));
                strankaDetail.setMesto(view.getClientPlace(redni_broj_edit_texta_u_grid_layout + 2));
                strankaDetail.setSlucaj(slucaj);

                Log.d(TAG, "saveCaseButtonClicked: detailsStranka " + strankaDetail.getIme_i_prezime()  + " " +  strankaDetail.getAdresa()  + " " + strankaDetail.getMesto());
                model.saveStrankaDetails(strankaDetail);

                redni_broj_edit_texta_u_grid_layout += 4;
            }
        }else {
            view.warrningMessage();
        }

        view.caseAddedMessage();
        view.goToPronadjeniSlucajAcitivty(slucaj);
    }
}
