package com.example.berka.advokatormlite.activities.krivica;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 06-Apr-18.
 */

public interface AddKrivicaContractMVP {

    interface View{

        //
        void addRedniBrojStranke(int redni_broj);

        void addEditTextZaImeStraneke();

        void addEditTextZaAdresu();

        void addEditTextZaMesto();

        //ArrayList<StrankaDetail> allClientsDetailValues();

        void loadSpinner(List<TabelaBodova> tabelaBodova);

        void goToPronadjeniSlucajAcitivty(Slucaj caseId);

        void emptyCasePasswordWarning();

        void numbersOnlyTestingWarning();

        void caseWithThisCaseAlreadyExists();

        void caseAddedMessage();

        String getClientName(int redni_broj_edit_texta_u_grid_layout);

        String getClientAddress(int i);

        String getClientPlace(int i);

        void warrningMessage();
    }

    interface Presenter{

        void addEditTextsProgramacly(int broj_stranaka);

        void setView(AddKrivicaContractMVP.View view);

        void loadSpinners(Postupak postupak);

        int radioButtonCurrentValue(String radiovalue);

        void saveCaseButtonClicked(String broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka);
    }

    interface Model{

        List<TabelaBodova> zaprecenjeKazneZaKrivicu(Postupak postupak);

        Slucaj saveSlucaj(String broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka);

        void saveStrankaDetails(StrankaDetail strankaDetail);
    }
}
