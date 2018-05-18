package com.example.berka.advokatormlite.activities.add_case.mvp_contracts;

import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentForKrivica;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 18-Apr-18.
 */

public interface KrivicaContract {

    interface View{

        //
        // void addRedniBrojStranke(int redni_broj);

//        void addEditTextZaImeStraneke();
//
//        void addEditTextZaAdresu();
//
//        void addEditTextZaMesto();

        //ArrayList<StrankaDetail> allClientsDetailValues();

//        void loadSpinner(List<TabelaBodova> tabelaBodova);
//
//        void goToPronadjeniSlucajAcitivty(Slucaj caseId);

        void emptyCasePasswordWarning();

        void numbersOnlyTestingWarning();

        void caseWithThisCaseAlreadyExists();

        void caseAddedMessage();

//        String getClientName(int redni_broj_edit_texta_u_grid_layout);
//
//        String getClientAddress(int i);
//
//        String getClientPlace(int i);

        void warrningMessage();

        void loadSpinner(List<TabelaBodova> zapreceneKazne);
    }

    interface Presenter{

        void setView(KrivicaContract.View view);

        void getZapreceneKazne(Postupak postupak);

        int radioButtonCurrentValue(String radiovalue);

        Slucaj saveCaseButtonClicked(int broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka);

        void detachView();

        void presenterWelcomeMessage();

    }

    interface Model{

        List<TabelaBodova> zaprecenjeKazneZaKrivicu(Postupak postupak);

        Slucaj saveSlucaj(Slucaj slucaj);

        void saveStrankaDetails(StrankaDetail strankaDetail);

        List<TabelaBodova> getZapreceneKazne(Postupak postupak);
    }
}
