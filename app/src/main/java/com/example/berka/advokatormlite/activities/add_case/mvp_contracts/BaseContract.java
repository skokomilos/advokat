package com.example.berka.advokatormlite.activities.add_case.mvp_contracts;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 08-Jun-18.
 */

public interface BaseContract {

    interface View<T> {

        void emptyCasePasswordWarning();

        void numbersOnlyTestingWarning();

        void thisCaseAlreadyExists();

        void caseAddedMessage();

        void addSlucaj();

        void loadSpinnerTabelaBodova(List<TabelaBodova> tabelaBodova);
    }

    interface Presenter<T> {

        void setView(T view);

        void detachView();

        Slucaj saveCaseButtonClicked(int broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka);

        Slucaj saveCaseButtonClicked(int broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int broj_stranaka);

        //ovaj verovatno nije potreban ovde, on treba da ide u prekrsaj i krivica contract, jer u parnica contract imam dva spinera pa ce tu ici druge metode
    }
}
