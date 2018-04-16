package com.example.berka.advokatormlite.activities.main;

import android.support.annotation.Nullable;

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

        if(view != null){
            if(slucajid.equals("")){
                view.showNoValuesTypedError();
            }else {
                Slucaj slucaj = model.getSlucaj(slucajid);
                if(slucaj.equals(null)){
                    view.displayNoPostupci();
                }else {
                    view.gotoFoundCaseFromFindCaseDialog(slucaj);
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
                switch (postupak.getId()) {
                    case 2:
                        //add krivica
                        view.startIntentKrivica(postupak, Integer.valueOf(broj_stranaka));
                        break;
                    case 4:
                        //add prekrsajni postupak
                        view.startIntentPrekrsaj(Integer.valueOf(broj_stranaka), postupak.getId());
                        break;
                    case 13:
                        //add isprave
                        view.startIntentIsprave(Integer.valueOf(broj_stranaka), postupak.getId());
                        break;
                    default:
                        //add sve ostale koji imaju procenjene i neprocenjene predmete
                        view.startIntentOstaleKojeImajuProcenjivoNeprocenjivo(Integer.valueOf(broj_stranaka), postupak.getId());
                        break;
                }
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