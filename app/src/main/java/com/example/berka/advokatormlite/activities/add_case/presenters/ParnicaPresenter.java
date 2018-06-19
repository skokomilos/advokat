package com.example.berka.advokatormlite.activities.add_case.presenters;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.ParnicaContract;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.VrsteParnica;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by berka on 18-Apr-18.
 */

public class ParnicaPresenter implements ParnicaContract.Presenter{

    private static final String TAG = "ParnicaPresenter";
    @Nullable
    ParnicaContract.View view;
    ParnicaContract.Model model;

    public ParnicaPresenter(ParnicaContract.Model model) {
        this.model = model;
    }

    @Override
    public void setView(ParnicaContract.View view) {
        this.view =  view;
    }

    @Override
    public void getVrsteParnica_ProcenjivoNeprocenjivo(Postupak postupak){
        Log.d(TAG, "getVrsteParnica_ProcenjivoNeprocenjivo: " + postupak.equals(null));
        List<VrsteParnica> vrsteParnicas = null;
         if(!postupak.equals(null)){
             try {
                 vrsteParnicas = model.loadZapreceneKazne(postupak);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
             if(!vrsteParnicas.equals(null)){
                view.loadSpinnerProcenjivoNeprocenjivo(vrsteParnicas);
            }
        }
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

    @Override
    public void getVrednostiTabeleBodovaZaOvajPostupak(VrsteParnica vrsteParnica, Postupak postupak){
        //procenjiva vrednost je za sve ista za sve vrste postupaka ali neprocenjiva nije ista za svako, ovih cetri vrednosti(vanparnica, uprava postupci, uprava sporovi, ostali) imaju unikatne vrednosti u DB u tabeli bodova.
        //broj 2 = neprocenjivo
        List<TabelaBodova> tabelaBodovaList = null;

            if(vrsteParnica.getId() == 2){
                if(postupak.getNazivpostupka().equals("Vanparnicni postupak") || postupak.getNazivpostupka().equals("Upravni postupak") || postupak.getNazivpostupka().equals("Upravni sporovi") || postupak.getNazivpostupka().equals("Ostali postupci")){
                    tabelaBodovaList = model.get_Unikatne_Neprocenjive_Vrednosti_Iz_TabeleBodova(vrsteParnica, postupak);
                }else {
                    tabelaBodovaList = model.get_Neprocenjive_Vrednosti_TabeleBodova_Koje_Nisu_Unikatne(vrsteParnica);
                }
            }else if(vrsteParnica.getId() == 3){
                //broj 3 = procenjivo, vrednosti su iste za sve postupke
                    tabelaBodovaList = model.get_Procenjene_Vrednosti_TabelaBodova(vrsteParnica);
            }
            
            view.loadSpinnerTabelaBodova(tabelaBodovaList);
    }

    @Override
    public void welcomeMessage() {

    }

    @Override
    public void detachView() {

        view = null;
    }

    @Override
    public Slucaj saveCaseButtonClicked(int broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka) {
        return null;
    }
}
