package com.example.berka.advokatormlite.activities.parnica;

import com.example.berka.advokatormlite.activities.krivica.AddKrivicaContractMVP;
import com.example.berka.advokatormlite.interfaces.DynamicalEditTexts;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.VrsteParnica;

import java.util.List;

/**
 * Created by berka on 15-Apr-18.
 */

public class AddParnicaContractMVP {

    interface View extends DynamicalEditTexts{

        void loadSpinnerPromenljivoNepromenljivo(List<VrsteParnica> vrstaParnica);

        void loadSpinner(List<TabelaBodova> tabelaBodova);
    }

    interface Presenter{

        void addEditTextsProgramacly(int broj_stranaka);

        void setView(AddParnicaContractMVP.View view);

        void loadSpinners(Postupak postupak);

        int radioButtonCurrentValue(String radiovalue);

        void saveCaseButtonClicked(String broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka);
    }

    interface Model{

    }
}
