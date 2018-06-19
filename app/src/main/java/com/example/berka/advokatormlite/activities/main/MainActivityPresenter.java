package com.example.berka.advokatormlite.activities.main;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentForKrivica;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentForParnica;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentPrekrsaj;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentUstavniSud;
import com.example.berka.advokatormlite.model.IzracunatTrosakRadnje;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by berka on 19-Feb-18.
 */

public class MainActivityPresenter implements MainActivityContractMVP.Presenter{

    private static final String TAG = "MainActivityPresenter";
    @Nullable
    private MainActivityContractMVP.View view;
    private MainActivityContractMVP.Model model;

    public MainActivityPresenter(MainActivityContractMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(MainActivityContractMVP.View view) {
        this.view = view;
    }


    @Override
    public void caseItemClicked(Slucaj slucaj) {

        if(view != null){
            if(slucaj.equals(null)){
                //univerzalni eror
                view.displayNoPostupci();
            }else {
                view.gotoFoundCaseFromAllCasesDialog(slucaj);
            }
        }
    }

    @Override
    public void findCaseButtonClicked(String slucajid) {

        Slucaj slucaj;

        if(view != null){
            if(slucajid.equals("")){
                view.showNoValuesTypedError();
            }else {
                slucaj = model.getSlucaj(slucajid);

                if(slucaj==null){
                    view.displayNoPostupci();
                }else {
                    view.gotoFoundCase(slucaj);
                }
            }
        }
    }

    @Override
    public void addCaseButtonClicked(Postupak postupak, String broj_stranaka) {

        if(view != null){
            if(broj_stranaka.trim().equals("")|| postupak.equals(null)){
                view.enterNumberOfClients();
            }else {
                view.startIntentAddCase(postupak, Integer.valueOf(broj_stranaka));
            }
        }
    }

    @Override
    public void getAllPostupci() {

        if(view != null) {
            List<Postupak> postupci = model.getPostupci();
            if (postupci.isEmpty()) {
                view.displayNoPostupci();
            } else {
                view.showAddCaseDialog(postupci);
            }
        }
    }

    @Override
    public void getAllCases() {

        if(view != null){
            List<Slucaj> slucajevi = model.getSlucajevi();
            List<Double> currentValues = calculateCurrentValuesForAllCasses(slucajevi);
            if(slucajevi.isEmpty()){
                view.displayNoPostupci();
            }else {
                view.showAllCasesDialog(slucajevi, currentValues);
            }
        }
    }

    @Override
    public List<Double> calculateCurrentValuesForAllCasses(List<Slucaj> slucajevi) {
        double trenutnaVrednost = 0;
        ArrayList<Double> listaSvhihTrenutnihCena = new ArrayList<>();
        ListIterator<Slucaj> iterator = null;
        iterator = slucajevi.listIterator();
        Slucaj s;
        while (iterator.hasNext()) {
            s = iterator.next();
            for (IzracunatTrosakRadnje i : s.getListaIzracunatihTroskovaRadnji()) {
                trenutnaVrednost += i.getCena_izracunate_jedinstvene_radnje();
            }
            listaSvhihTrenutnihCena.add(Double.valueOf(trenutnaVrednost));
            trenutnaVrednost = 0;
        }
        return listaSvhihTrenutnihCena;
    }

    @Override
    public void prefUserDialogCreated(){

        if(view != null){

            final String userName = model.getCurentUserName();
            view.updateLawyerName(userName);

            final String address = model.getCurrentAddress();
            view.updateLawyerAddress(address);

            final String place = model.getCurretnPlace();
            view.updateLawyerPlace(place);
        }
    }


    @Override
    public void prefUserDialogSaveData(String name, String address, String  place){

        if (view != null){

            model.updateUserInfo(name, address, place);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }
}